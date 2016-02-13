
package com.agilarity.osmo.requirement.model;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class NoRequirementAnnotations {
  // The requirements are used by RequirementAnnotationListener, not the model directly.
  @SuppressWarnings("unused")
  private final Requirements requirements;

  public NoRequirementAnnotations(final Requirements requirements) {
    super();
    this.requirements = requirements;
  }

  @TestStep
  public void doNotAssertAnything() {
  }
}
