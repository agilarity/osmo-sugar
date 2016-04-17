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

import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.agilarity.osmo.requirement.errors.MissingRequirementStepException;
import com.agilarity.osmo.requirement.errors.MissingRequirementsObjectException;
import com.agilarity.osmo.requirement.model.CoverAndFailStep;
import com.agilarity.osmo.requirement.model.CoverAndFailTest;
import com.agilarity.osmo.requirement.model.DoSomething;
import com.agilarity.osmo.requirement.model.NamedStepWithRequirement;
import com.agilarity.osmo.requirement.model.NoRequiremensOject;
import com.agilarity.osmo.requirement.model.NoRequirementAnnotations;
import com.agilarity.osmo.requirement.model.NoRequirementStep;
import com.agilarity.osmo.requirement.model.StepWithRequirement;
import com.agilarity.osmo.requirement.model.ValuedStepWithRequirement;
import com.agilarity.osmo.requirement.model.VerifySomething;
import com.agilarity.osmo.requirement.name.IdStepMethodNamingStrategy;

import osmo.common.OSMOException;
import osmo.tester.OSMOTester;
import osmo.tester.model.Requirements;

public class RequirementsListenerTest {
  private static final String SHOULD_STILL_COVER_TEST_REQUIREMENT = "CoverAndFailTest.shouldStillCoverTestRequirement";
  private static final String SHOULD_COVER_AND_FAIL_TEST = "CoverAndFailTest.shouldCoverAndFailTest";
  private static final String SHOULD_ALSO_COVER_AND_FAIL_STEP = "CoverAndFailStep.shouldAlsoCoverAndFailStep";
  private static final String SHOULD_COVER_AND_FAIL_STEP = "CoverAndFailStep.shouldCoverAndFailStep";
  private static final String SHOULD_DO_SOMETHING_ELSE = "DoSomething.shouldDoSomethingElse";
  private static final String R101 = "R101:DoSomething.shouldDoSomething";
  private RequirementAnnotationListener listener;
  private Requirements requirements;
  private OSMOTester osmoTester;

  @BeforeMethod
  public void before() throws NoSuchMethodException, SecurityException {
    requirements = new Requirements();
    osmoTester = new OSMOTester();
    listener = new RequirementAnnotationListener(osmoTester.getConfig().getFactory(),
        new IdStepMethodNamingStrategy());
    osmoTester.addListener(listener);
  }

  @Test
  public void shouldAddRequirements() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new DoSomething(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one requirement for each annotation
    assertThat(requirements.getRequirements()).containsAll(asList(R101, SHOULD_DO_SOMETHING_ELSE));
  }

  @Test
  public void shouldVerifySomething() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new DoSomething(requirements));

    // AND a verification only model for DoSomething
    osmoTester.addModelObject(new VerifySomething(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one requirement for each annotation
    assertThat(requirements.getRequirements()).containsAll(
        asList(R101, SHOULD_DO_SOMETHING_ELSE, "DoSomething.shouldVerifySomething"));
  }

  @Test
  public void shouldAddStepWithRequirement() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new StepWithRequirement(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one requirement for each annotation
    assertThat(requirements.getRequirements()).containsAll(
        asList("StepWithRequirement.stepWithRequirement"));
  }

  @Test
  public void shouldAddNamedStepWithRequirement() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new NamedStepWithRequirement(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one requirement for each annotation
    assertThat(requirements.getRequirements()).containsAll(
        asList("MyStepWithRequirement.stepWithRequirement"));
  }

  @Test
  public void shouldAddValuedStepWithRequirement() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new ValuedStepWithRequirement(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one requirement for each annotation
    assertThat(requirements.getRequirements()).containsAll(
        asList("MyStepWithRequirement.stepWithRequirement"));
  }

  @Test
  public void shouldGetAnnotatedRequirements() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new DoSomething(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one annotated requirement for each annotation
    final List<String> names = listener.getAnnotatedRequirements().stream()
        .map(AnnotatedRequirement::getName).collect(Collectors.toList());
    assertThat(names).contains(R101, SHOULD_DO_SOMETHING_ELSE);
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
      // THEN the requirements will be uncovered
      assertThat(requirements.getMissingCoverage()).containsAll(
          asList(SHOULD_COVER_AND_FAIL_STEP, SHOULD_ALSO_COVER_AND_FAIL_STEP));
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
      // THEN the requirements will be uncovered
      assertThat(requirements.getMissingCoverage()).contains(SHOULD_COVER_AND_FAIL_TEST);
    }

    // AND the passing requirement will still be covered
    assertThat(requirements.getFullCoverage()).contains(SHOULD_STILL_COVER_TEST_REQUIREMENT);
  }

  @Test
  public void shouldRemeberTestStatus() {
    // GIVEN a model that fails
    osmoTester.addModelObject(new CoverAndFailTest(requirements));

    // AND requirements are covered in the model

    // WHEN the tests are generated
    try {
      osmoTester.generate(1);
      fail("Expected OSMOException");
    } catch (final OSMOException e) {
      // THEN the failing requirement will be remembered
      assertThat(listener.getFailingRequirement().getMethod()).isEqualTo("shouldCoverAndFailTest");
    }

    // AND the passing requirements will be remembered
    final List<String> passingMethods = listener.getPassingRequirements().stream()
        .map(AnnotatedRequirement::getMethod).collect(Collectors.toList());
    assertThat(passingMethods).containsExactly("shouldStillCoverTestRequirement");
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
      assertThat(e.getMessage()).isEqualTo(
          "Add step to @Requirement or end of method for [NoRequirementStep.doesNotMatchStep]");
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

  @Test
  public void shouldRememberFailingTestCase() {
    // GIVEN a model that fails in the step
    osmoTester.addModelObject(new CoverAndFailStep(requirements));

    // WHEN the tests are generated
    try {
      osmoTester.generate(1);
      fail("Expected OSMOException");
    } catch (final OSMOException e) {
      // THEN the failing test case will be remembered
      assertThat(listener.getFailingTestCase().getCurrentStep().getName()).isEqualTo(
          "CoverAndFailStep");
    }
  }

  @Test
  public void shouldRememberError() {
    // GIVEN a model that fails in the step
    osmoTester.addModelObject(new CoverAndFailStep(requirements));

    // WHEN the tests are generated
    try {
      osmoTester.generate(1);
      fail("Expected OSMOException");
    } catch (final OSMOException e) {
      // THEN the error will be remembered
      assertThat(listener.getError().getMessage()).isEqualTo("Fail in a step");
    }
  }
}
