
package com.agilarity.osmo.requirement.name;

/**
 * Provides a plug-in point for the requirement name.
 */
public interface RequirementNamingStrategy {

  /**
   * @return the requirement name.
   */
  String buildName(final String id, final String step, final String method);
}
