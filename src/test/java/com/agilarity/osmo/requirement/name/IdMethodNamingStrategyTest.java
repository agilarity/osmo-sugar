
package com.agilarity.osmo.requirement.name;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IdMethodNamingStrategyTest {
  private RequirementNamingStrategy uut;

  @BeforeMethod
  public void before() {
    uut = new IdMethodNamingStrategy();
  }

  @Test
  public void shouldShowId() {
    assertThat(uut.buildName("id", "step", "method")).isEqualTo("id:method");
  }

  @Test
  public void shouldShowMethod() {
    assertThat(uut.buildName("", "step", "method")).isEqualTo("method");
  }
}
