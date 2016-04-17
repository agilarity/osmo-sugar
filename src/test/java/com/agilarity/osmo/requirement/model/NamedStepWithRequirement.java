
package com.agilarity.osmo.requirement.model;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class NamedStepWithRequirement {
  @SuppressWarnings("unused")
  private final Requirements requirements;

  public NamedStepWithRequirement(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @TestStep(name = "MyStepWithRequirement")
  @Requirement
  public void stepWithRequirement() {
  }
}
