
package com.agilarity.osmo.requirement;

import com.agilarity.osmo.requirement.name.RequirementNamingStrategy;

/**
 * Holds information about the annotated requirement.
 */
public class AnnotatedRequirement {
  private final String id;
  private final String step;
  private final String method;
  private final transient RequirementNamingStrategy requirementNamingStrategy;

  /**
   * Capture requirement information.
   */
  public AnnotatedRequirement(final RequirementNamingStrategy requirementNamingStrategy,
      final String id, final String step, final String method) {
    super();
    this.id = id;
    this.step = step;
    this.method = method;
    this.requirementNamingStrategy = requirementNamingStrategy;
  }

  /**
   * @return the id.
   */
  public String getId() {
    return id;
  }

  /**
   * @return the step.
   */
  public String getStep() {
    return step;
  }

  /**
   * @return the annotatedMethod.
   */
  public String getMethod() {
    return method;
  }

  /**
   * @return the @Requirement value if present or the method otherwise.
   */
  public String getName() {
    return requirementNamingStrategy.buildName(getId(), getStep(), getMethod());
  }
}
