
package com.agilarity.osmo.requirement.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.agilarity.osmo.requirement.errors.InvalidRequirementAnnotationException;

/**
 * Provide reflective access to annotation properties.
 */
public final class AnnotationAccesor {

  /**
   * @param annotation The annotation.
   */
  private AnnotationAccesor() {
    super();
  }

  /**
   * @return The requirement identifier.
   */
  public static String getValue(final Annotation annotation) {
    return getString(annotation, "value");
  }

  /**
   * @return The step name.
   */
  public static String getStep(final Annotation annotation) {
    return getString(annotation, "step");
  }

  private static String getString(final Annotation annotation, final String methodName) {
    try {
      final Method method = annotation.getClass().getMethod(methodName);
      return (String) method.invoke(annotation);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e) {
      throw new InvalidRequirementAnnotationException(annotation, methodName, e);
    }
  }
}
