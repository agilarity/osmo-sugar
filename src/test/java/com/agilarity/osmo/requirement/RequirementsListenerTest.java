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

import java.util.Optional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.agilarity.osmo.requirement.model.CoverAndFailStep;
import com.agilarity.osmo.requirement.model.CoverAndFailTest;
import com.agilarity.osmo.requirement.model.DoSomething;
import com.agilarity.osmo.requirement.model.NoRequiremensOject;
import com.agilarity.osmo.requirement.model.NoRequirementAnnotations;
import com.agilarity.osmo.requirement.model.NoRequirementStep;

import osmo.common.OSMOException;
import osmo.tester.OSMOTester;
import osmo.tester.model.Requirements;

public class RequirementsListenerTest {
  private RequirementAnnotationListener listener;
  private Requirements requirements;
  private OSMOTester osmoTester;

  @BeforeMethod
  public void before() throws NoSuchMethodException, SecurityException {
    requirements = new Requirements();
    osmoTester = new OSMOTester();
    listener = new RequirementAnnotationListener();
    osmoTester.addListener(listener);
  }

  @Test
  public void shouldAddRequirements() {
    // GIVEN a model with annotated requirements
    osmoTester.addModelObject(new DoSomething(requirements));

    // WHEN the tests are generated
    osmoTester.generate(1);

    // THEN the there will be one requirement for each annotation
    assertThat(requirements.getRequirements()).containsAll(asList("R101", "shouldDoSomethingElse"));
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
          asList("shouldCoverAndFailStep", "shouldAlsoCoverAndFailStep"));
    }
  }

  @Test
  public void shouldUncoverRequirement() {
    // GIVEN a model that fails
    osmoTester.addModelObject(new CoverAndFailTest(requirements));
    String failingMethod = "shouldCoverAndFailTest";
    String passingMethod = "shouldStillCoverTestRequirement";

    // AND requirements are covered in the model

    // WHEN the tests are generated
    try {
      osmoTester.generate(1);
      fail("Expected OSMOException");
    } catch (final OSMOException e) {
      // THEN the requirements will be covered
      assertThat(requirements.getMissingCoverage()).contains(failingMethod);

      // AND the failing requirement will be remembered
      assertThat(listener.getFailingRequirement().getMethod()).isEqualTo(failingMethod);
    }

    // AND the passing requirement will still be covered
    assertThat(requirements.getFullCoverage()).contains(passingMethod);

    // AND the passing requirement will be remembered
    Optional<AnnotatedRequirement> expected = listener.getPassingRequirements().stream()
        .filter(requirement -> requirement.getMethod().equals(passingMethod)).findFirst();
    assertThat(expected).isPresent();
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
}
