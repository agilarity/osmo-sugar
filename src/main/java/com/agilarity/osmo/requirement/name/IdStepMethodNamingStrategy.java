
package com.agilarity.osmo.requirement.name;

import static java.lang.String.format;

/**
 * Provides a verbose naming strategy.
 */
public class IdStepMethodNamingStrategy implements RequirementNamingStrategy {

  /**
   * @return the id or the method.
   */
  @Override
  public String buildName(final String id, final String step, final String method) {
    return format("%s:%s.%s", id, step, method);
  }
}
