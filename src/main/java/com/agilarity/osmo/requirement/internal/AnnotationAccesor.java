
package com.agilarity.osmo.requirement.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.agilarity.osmo.requirement.errors.InvalidRequirementAnnotationException;

/**
 * Provide reflective access to annotation properties.
 */
public class AnnotationAccesor {
  private final transient Annotation annotation;

  /**
   * @param annotation The annotation.
   */
  public AnnotationAccesor(final Annotation annotation) {
    super();
    this.annotation = annotation;
  }

  /**
   * @return The requirement identifier.
   */
  public String value() {
    return getString("value");
  }

  /**
   * @return The step name.
   */
  public String step() {
    return getString("step");
  }

  private String getString(final String methodName) {
    try {
      final Method method = annotation.getClass().getMethod(methodName);
      return (String) method.invoke(annotation);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      throw new InvalidRequirementAnnotationException(annotation, methodName, e);
    }
  }
}
