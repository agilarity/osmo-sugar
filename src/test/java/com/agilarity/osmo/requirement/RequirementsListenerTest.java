/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Joseph A. Cruz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agilarity.osmo.requirement;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import osmo.common.OSMOException;
import osmo.tester.OSMOTester;
import osmo.tester.annotation.Post;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class RequirementsListenerTest {
  private Requirements requirements;
  private OSMOTester osmoTester;

  @BeforeMethod
  public void before() throws NoSuchMethodException, SecurityException {
    requirements = new Requirements();
    osmoTester = new OSMOTester();
    osmoTester.addListener(new RequirementAnnotationListener());
  }

  @Test
  public void shouldAddRequirements() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new DoSomething(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one requirement for each annotation
    assertThat(requirements.getRequirements()).containsAll(
        asList("DoSomething.shouldDoSomething", "DoSomething.shouldDoSomethingElse"));
  }

  @Test
  public void shouldCoverRequirements() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new DoSomething(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the requirements will be covered
    assertThat(requirements.getMissingCoverage()).isEmpty();
  }

  @Test
  public void shouldUncoverStepRequirements() {
    // GIVEN a model that fails in the step
    osmoTester.addModelObject(new CoverAndFailStep(requirements));

    // AND requirements are covered in the model

    // WHEN the tests are generated
    try {
      osmoTester.generate(1);
      fail("Expected OSMOException");
    } catch (final OSMOException e) {
      // THEN the requirements will be covered
      assertThat(requirements.getMissingCoverage()).containsAll(
          asList("CoverAndFailStep.shouldCoverAndFailStep",
              "CoverAndFailStep.shouldAlsoCoverAndFailStep"));
    }
  }

  @Test
  public void shouldUncoverRequirement() {
    // GIVEN a model that fails
    osmoTester.addModelObject(new CoverAndFailTest(requirements));

    // AND requirements are covered in the model

    // WHEN the tests are generated
    try {
      osmoTester.generate(1);
      fail("Expected OSMOException");
    } catch (final OSMOException e) {
      // THEN the requirements will be covered
      assertThat(requirements.getMissingCoverage()).contains(
          "CoverAndFailTest.shouldCoverAndFailTest");
    }

    assertThat(requirements.getFullCoverage()).contains(
        "CoverAndFailTest.shouldStillCoverTestRequirement");
  }

  @Test
  public void shouldNotRequireAnnotations() {
    // GIVEN a model without requirement annotations
    osmoTester.addModelObject(new NoRequirementAnnotations(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the requirements will be covered
    assertThat(requirements.getMissingCoverage()).isEmpty();
  }

  @Test
  public void shouldRequireStep() {
    // GIVEN a model with a requirement that does not match a step
    osmoTester.addModelObject(new NoRequirementStep(requirements));

    try {
      // WHEN the tests are generated
      osmoTester.generate(1);
      fail("Expected MissingRequirementStepException");
    } catch (final MissingRequirementStepException e) {
      // THEN the missing step error will be reported
      assertThat(e.getMessage())
          .isEqualTo(
              "Add step [MyStep] to @Requirement or end of method for [NoRequirementStep.doesNotMatchStep]");
    }
  }

  @Test
  public void shouldRequireRequirementsObject() {
    // GIVEN a model without a requirements object
    osmoTester.addModelObject(new NoRequiremensOject());

    try {
      // WHEN the tests are generated
      osmoTester.generate(1);
      fail("Expected MissingRequirementsObjectException");
    } catch (final MissingRequirementsObjectException e) {
      // THEN the missing requirements object error will be reported
      assertThat(e.getMessage()).isEqualTo("At least one model must have a Requirements object.");
    }
  }

  public class DoSomething {
    @SuppressWarnings("unused")
    private final Requirements requirements;

    public DoSomething(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void doSomething() {
    }

    @Requirement
    public void shouldDoSomething() {
    }

    @Requirement(step = "doSomething")
    public void shouldDoSomethingElse() {
    }
  }

  public class NoRequirementAnnotations {
    // The requirements are used by RequirementAnnotationListener, not the model directly.
    @SuppressWarnings("unused")
    private final Requirements requirements;

    public NoRequirementAnnotations(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void doNotAssertAnything() {
    }
  }

  public class NoRequirementStep {
    // The requirements are used by RequirementAnnotationListener, not the model directly.
    @SuppressWarnings("unused")
    private final Requirements requirements;

    public NoRequirementStep(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void myStep() {
    }

    @Requirement
    public void doesNotMatchStep() {

    }
  }

  public class NoRequiremensOject {

    @TestStep
    public void noRequiremensOject() {
    }

    @Requirement
    public void shouldNoRequiremensOject() {

    }
  }

  public class CoverAndFailTest {
    private final Requirements requirements;

    public CoverAndFailTest(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void coverAndFailTest() {
      requirements.covered("CoverAndFailTest.shouldCoverAndFailTest");
    }

    @Post
    @Requirement
    public void shouldCoverAndFailTest() {
      fail("Fail in a requirement method");
    }

    @Post("coverAndFailTest")
    @Requirement(step = "coverAndFailTest")
    public void shouldStillCoverTestRequirement() {
    }
  }

  public class CoverAndFailStep {
    private final Requirements requirements;

    public CoverAndFailStep(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void coverAndFailStep() {
      requirements.covered("CoverAndFailStep.shouldCoverAndFailStep");
      fail("Fail in a step");
    }

    @Post
    @Requirement
    public void shouldCoverAndFailStep() {
    }

    @Post("coverAndFailStep")
    @Requirement(step = "coverAndFailStep")
    public void shouldAlsoCoverAndFailStep() {
    }
  }
}
