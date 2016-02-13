
package com.agilarity.osmo.requirement.model;

import static org.assertj.core.api.Assertions.fail;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.Post;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class CoverAndFailStep {
  private final Requirements requirements;

  public CoverAndFailStep(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @TestStep
  public void coverAndFailStep() {
    requirements.covered("CoverAndFailStep.shouldCoverAndFailStep");
    fail("Fail in a step");
  }

  @Post
  @Requirement
  public void shouldCoverAndFailStep() {
  }

  @Post("coverAndFailStep")
  @Requirement(step = "coverAndFailStep")
  public void shouldAlsoCoverAndFailStep() {
  }
}
