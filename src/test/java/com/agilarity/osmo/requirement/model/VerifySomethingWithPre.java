
package com.agilarity.osmo.requirement.model;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.Pre;
import osmo.tester.model.Requirements;

public class VerifySomethingWithPre {
  @SuppressWarnings("unused")
  private final Requirements requirements;

  public VerifySomethingWithPre(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @Pre("DoSomething")
  @Requirement
  public void shouldVerifySomethingWithPre() {
  }
}
