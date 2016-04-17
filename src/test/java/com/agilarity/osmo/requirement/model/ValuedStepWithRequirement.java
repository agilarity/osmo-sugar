
package com.agilarity.osmo.requirement.model;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class ValuedStepWithRequirement {
  @SuppressWarnings("unused")
  private final Requirements requirements;

  public ValuedStepWithRequirement(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @TestStep("MyStepWithRequirement")
  @Requirement
  public void stepWithRequirement() {
  }
}
