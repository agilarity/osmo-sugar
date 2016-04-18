
package com.agilarity.osmo.requirement.model;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class StepWithRequirement {
  @SuppressWarnings("unused")
  private final Requirements requirements;

  public StepWithRequirement(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @TestStep
  @Requirement
  public void stepWithRequirement() {
  }
}
