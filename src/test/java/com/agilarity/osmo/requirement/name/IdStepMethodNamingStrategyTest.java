
package com.agilarity.osmo.requirement.name;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IdStepMethodNamingStrategyTest {
  private RequirementNamingStrategy uut;

  @BeforeMethod
  public void before() {
    uut = new IdStepMethodNamingStrategy();
  }

  @Test
  public void shouldShowIdStepMethod() {
    assertThat(uut.buildName("id", "step", "method")).isEqualTo("id:step.method");
  }

  @Test
  public void shouldShowStepMethod() {
    assertThat(uut.buildName("", "step", "method")).isEqualTo("step.method");
  }
}
