
package com.agilarity.osmo.requirement;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.agilarity.osmo.requirement.internal.GoodRequirementAnnotation;

public class ConfigureRequirementAnnotationTest {
  private RequirementAnnotationListener listener;

  @BeforeMethod
  public void before() {
    listener = new RequirementAnnotationListener(null);
  }

  @Test
  public void shouldSpecifyRequirementAnnotation() {
    // WHEN the requirement annotation has not been specified

    // THEN the default requirement annotation will be available
    assertThat(listener.getRequirementAnnotationClass()).isEqualTo(Requirement.class);
  }

  @Test
  public void shouldChangeRequirementAnnotation() {
    // WHEN the requirement annotation has been specified
    listener.setRequirementAnnotationClass(GoodRequirementAnnotation.class);

    // THEN the requirement annotation will be the specified annotation
    assertThat(listener.getRequirementAnnotationClass()).isEqualTo(GoodRequirementAnnotation.class);
  }
}
