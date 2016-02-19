
package com.agilarity.osmo.requirement.name;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StepMethodNamingStrategyTest {
  private RequirementNamingStrategy uut;

  @BeforeMethod
  public void before() {
    uut = new StepMethodNamingStrategy();
  }

  @Test
  public void shouldDefaultToStepMethod() {
    assertThat(uut.buildName("aaa", "bbb", "ccc")).isEqualTo("bbb.ccc");
  }
}
