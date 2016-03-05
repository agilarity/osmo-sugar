
package com.agilarity.osmo.requirement.internal;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.lang.annotation.Annotation;

import org.testng.annotations.Test;

import com.agilarity.osmo.requirement.errors.InvalidRequirementAnnotationException;

public class AnnotationAccesorTest {
  private AnnotationAccesor accessor;
  private Annotation annotation;

  @Test
  public void shouldGetValue() throws NoSuchMethodException, SecurityException {
    // WHEN the annotation is valid
    accessor = new AnnotationAccesor(getAnnotation("good", GoodRequirementAnnotation.class));

    // THEN the value will match
    assertThat(accessor.value()).isEqualTo("R1");
  }

  @Test
  public void shouldGetStep() throws NoSuchMethodException, SecurityException {
    // WHEN the annotation is valid
    accessor = new AnnotationAccesor(getAnnotation("good", GoodRequirementAnnotation.class));

    // THEN the step will match
    assertThat(accessor.step()).isEqualTo("StepOne");
  }

  @Test
  public void shouldReportMissingValue() throws NoSuchMethodException, SecurityException {
    // WHEN the annotation is missing the value
    annotation = getAnnotation("missingValue", BadRequirementAnnotationMissingValue.class);
    accessor = new AnnotationAccesor(annotation);

    try {
      accessor.value();
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
    accessor = new AnnotationAccesor(annotation);
    try {
      accessor.step();
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
