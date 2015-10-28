
package com.agilarity.osmo.requirement.name;

/**
 * Provides a simple naming strategy.
 */
public class SimpleRequirementNamingStrategy implements RequirementNamingStrategy {

  /**
   * @return the id or the method.
   */
  @Override
  public String buildName(final String id, final String step, final String method) {
    if (id.isEmpty()) {
      return method;
    }

    return id;
  }
}
