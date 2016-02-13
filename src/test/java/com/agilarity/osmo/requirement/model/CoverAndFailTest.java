
package com.agilarity.osmo.requirement.model;

import static org.assertj.core.api.Assertions.fail;

import com.agilarity.osmo.requirement.Requirement;

import osmo.tester.annotation.Post;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class CoverAndFailTest {
  private final Requirements requirements;

  public CoverAndFailTest(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @TestStep
  public void coverAndFailTest() {
    requirements.covered("CoverAndFailTest.shouldCoverAndFailTest");
  }

  @Post
  @Requirement
  public void shouldCoverAndFailTest() {
    fail("Fail in a requirement method");
  }

  @Post("coverAndFailTest")
  @Requirement(step = "coverAndFailTest")
  public void shouldStillCoverTestRequirement() {
  }
}
