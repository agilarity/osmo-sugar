
package com.agilarity.osmo.requirement.model;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

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
