
package com.agilarity.osmo.requirement.internal;

import static com.agilarity.osmo.requirement.internal.AnnotationAccesor.getStep;
import static com.agilarity.osmo.requirement.internal.AnnotationAccesor.getValue;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.isPrivate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.testng.annotations.Test;

import com.agilarity.osmo.requirement.errors.InvalidRequirementAnnotationException;

public class AnnotationAccesorTest {
  private Annotation annotation;

  @Test
  public void shouldGetValue() throws NoSuchMethodException, SecurityException {
    // WHEN the annotation is valid
    annotation = getAnnotation("good", GoodRequirementAnnotation.class);

    // THEN the value will match
    assertThat(getValue(annotation)).isEqualTo("R1");
  }

  @Test
  public void shouldGetStep() throws NoSuchMethodException, SecurityException {
    // WHEN the annotation is valid
    annotation = getAnnotation("good", GoodRequirementAnnotation.class);

    // THEN the step will match
    assertThat(getStep(annotation)).isEqualTo("StepOne");
  }

  @Test
  public void shouldReportMissingValue() throws NoSuchMethodException, SecurityException {
    // WHEN the annotation is missing the value
    annotation = getAnnotation("missingValue", BadRequirementAnnotationMissingValue.class);

    try {
      getValue(annotation);
      fail("Expected InvalidRequirementAnnotationException");
    } catch (InvalidRequirementAnnotationException e) {
      String error = format("%s annotation is missing the value property.",
          BadRequirementAnnotationMissingValue.class.getName());

      // THEN the error will be reported
      assertThat(e.getMessage()).isEqualTo(error);

      // AND the cause will be remembered
      assertThat(e.getCause()).isNotNull();
    }
  }

  @Test
  public void shouldReportMissingStep() throws NoSuchMethodException, SecurityException {
    // WHEN the annotation is missing the step
    annotation = getAnnotation("missingStep", BadRequirementAnnotationMissingStep.class);
    try {
      getStep(annotation);
      fail("Expected InvalidRequirementAnnotationException");
    } catch (InvalidRequirementAnnotationException e) {
      String error = format("%s annotation is missing the step property.",
          BadRequirementAnnotationMissingStep.class.getName());

      // THEN the error will be reported
      assertThat(e.getMessage()).isEqualTo(error);

      // AND the cause will be remembered
      assertThat(e.getCause()).isNotNull();
    }
  }

  @Test
  public void shouldHideConstructor() throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {
    final Constructor<AnnotationAccesor> constructor = AnnotationAccesor.class
        .getDeclaredConstructor();
    assertThat(isPrivate(constructor.getModifiers())).isTrue();
    constructor.setAccessible(true);
    constructor.newInstance();
  }

  public Annotation getAnnotation(String method, Class<? extends Annotation> annotationClass)
      throws NoSuchMethodException, SecurityException {
    return getClass().getMethod(method).getAnnotation(annotationClass);
  }

  @GoodRequirementAnnotation(value = "R1", step = "StepOne")
  public void good() {
  }

  @BadRequirementAnnotationMissingValue(step = "StepTwo")
  public void missingValue() {
  }

  @BadRequirementAnnotationMissingStep(value = "R3")
  public void missingStep() {
  }
}
