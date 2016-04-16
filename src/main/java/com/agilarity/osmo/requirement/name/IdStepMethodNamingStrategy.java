
package com.agilarity.osmo.requirement.name;

import static java.lang.String.format;

/**
 * Provides a verbose naming strategy.
 */
public class IdStepMethodNamingStrategy implements RequirementNamingStrategy {

  /**
   * @return the requirement name.
   */
  @Override
  public String buildName(final String id, final String step, final String method) {
    final String identifier = id.isEmpty() ? id : id + ":";
    return format("%s%s.%s", identifier, step, method);
  }
}
