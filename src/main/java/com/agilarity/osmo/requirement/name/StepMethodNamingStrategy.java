
package com.agilarity.osmo.requirement.name;

import static java.lang.String.format;

/**
 * Provides a simple naming strategy.
 */
public class StepMethodNamingStrategy implements RequirementNamingStrategy {

  /**
   * @return the id or the method.
   */
  @Override
  public String buildName(final String id, final String step, final String method) {
    return format("%s.%s", step, method);
  }
}
