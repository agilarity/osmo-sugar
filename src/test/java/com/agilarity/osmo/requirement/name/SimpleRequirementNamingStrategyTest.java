
package com.agilarity.osmo.requirement.name;

import static com.agilarity.osmo.requirement.Requirement.DEFAULT_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SimpleRequirementNamingStrategyTest {
  private RequirementNamingStrategy uut;

  @BeforeMethod
  public void before() {
    uut = new SimpleRequirementNamingStrategy();
  }

  @Test
  public void shouldPreferId() {
    assertThat(uut.buildName("AAA", "bbb", "ccc")).isEqualTo("AAA");
  }

  @Test
  public void shouldDefaultToStepMethod() {
    assertThat(uut.buildName(DEFAULT_VALUE, "bbb", "ccc")).isEqualTo("ccc");
  }
}
