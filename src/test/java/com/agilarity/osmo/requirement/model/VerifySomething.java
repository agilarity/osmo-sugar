
package com.agilarity.osmo.requirement.model;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.model.Requirements;

public class VerifySomething {
  @SuppressWarnings("unused")
  private final Requirements requirements;

  public VerifySomething(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @Requirement(step = "DoSomething")
  public void shouldVerifySomething() {
  }
}
