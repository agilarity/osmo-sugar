
package com.agilarity.osmo.requirement.model;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

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

  @Requirement("R101")
  public void shouldDoSomething() {
  }

  @Requirement(step = "doSomething")
  public void shouldDoSomethingElse() {
  }
}
