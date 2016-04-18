
package com.agilarity.osmo.requirement.model;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.Post;
import osmo.tester.model.Requirements;

public class VerifySomethingWithPost {
  @SuppressWarnings("unused")
  private final Requirements requirements;

  public VerifySomethingWithPost(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @Post("DoSomething")
  @Requirement
  public void shouldVerifySomethingWithPost() {
  }
}
