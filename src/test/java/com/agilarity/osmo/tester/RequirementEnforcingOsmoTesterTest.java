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

package com.agilarity.osmo.tester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class RequirementEnforcingOsmoTesterTest {
  private RequirementEnforcingOsmoTester tester;

  @BeforeMethod
  public void createUnitUnderTest() {
    tester = new RequirementEnforcingOsmoTester();
  }

  @Test
  public void shouldPassWithoutRequirementsObject() {
    // GIVEN a model without a requirements object
    tester.addModelObject(new SimpleModel());

    // WHEN the tests are generated
    tester.generate(1);

    // THEN the test will not fail because of missing coverage
  }

  @Test
  public void shouldPassWithoutRequirements() {
    // GIVEN no requirements are added
    tester.addModelObject(new EmptyRequirementsModel(new Requirements()));

    // WHEN the tests are generated
    tester.generate(1);

    // THEN the tests will not fail because of missing coverage
  }

  @Test
  public void shouldPassWithCoverage() {
    // GIVEN a model with requirements
    tester.addModelObject(new RequirementsModel(new Requirements()));

    // WHEN the tests are generated
    tester.generate(1);

    // THEN the tests will not fail because of missing coverage
  }

  @Test
  public void shouldFailWithMissingCoverage() {
    // GIVEN a model with requirements
    tester.addModelObject(new MissingCoverageModel(new Requirements()));

    // WHEN the tests are generated
    try {
      tester.generate(1);
      fail("Expected MissingCoverageException");
    } catch (final MissingCoverageException e) {
      assertThat(e.getMessage()).isEqualTo("Not covered [R3, R4]");
    }

    // THEN the tests will not fail because of missing coverage
  }

  public class SimpleModel {

    @TestStep
    public void simpleStep() {

    }
  }

  public class EmptyRequirementsModel {
    @SuppressWarnings("unused")
    private final Requirements requirements;

    public EmptyRequirementsModel(final Requirements requirements) {
      super();
      this.requirements = requirements;
    }

    @TestStep
    public void stepWithoutRequirements() {

    }
  }

  public class RequirementsModel {
    private final Requirements requirements;

    public RequirementsModel(final Requirements requirements) { // NOCS
      super();
      this.requirements = requirements;
      this.requirements.add("R1");
      this.requirements.add("R2");
    }

    @TestStep
    public void stepCoveringRequirements() {
      requirements.covered("R1");
      requirements.covered("R2");
    }
  }

  public class MissingCoverageModel {
    private final Requirements requirements;

    public MissingCoverageModel(final Requirements requirements) { // NOCS
      super();
      this.requirements = requirements;
      this.requirements.add("R3");
      this.requirements.add("R4");
    }

    @TestStep
    public void stepMissingCoverage() {
    }
  }
}
