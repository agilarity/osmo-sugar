
package com.agilarity.osmo.requirement.internal;

import static com.agilarity.osmo.requirement.internal.AnnotationAccesor.getStep;
import static com.agilarity.osmo.requirement.internal.AnnotationAccesor.getValue;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

import com.agilarity.osmo.requirement.AnnotatedRequirement;
import com.agilarity.osmo.requirement.errors.MissingRequirementStepException;
import com.agilarity.osmo.requirement.name.RequirementNamingStrategy;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.FSM;

/**
 * Build the list of annotated requirements.
 */
public class AnnotationRequirementsBuilder {
  private final transient RequirementNamingStrategy requirementNamingStrategy;
  private final transient Class<? extends Annotation> requirementAnnotationClass;
  private transient Deque<Method> danglingMethods;
  private transient Collection<AnnotatedRequirement> annotatedRequirements;

  /**
   * Create builder.
   */
  public AnnotationRequirementsBuilder(final FSM fsm,
      final RequirementNamingStrategy requirementNamingStrategy,
      final Class<? extends Annotation> requirementAnnotationClass) {
    super();
    this.requirementNamingStrategy = requirementNamingStrategy;
    this.requirementAnnotationClass = requirementAnnotationClass;
    buildAnnotatedRequirements(fsm);
  }

  /**
   * @return the annotatedRequirements.
   */
  public Collection<AnnotatedRequirement> getAnnotatedRequirements() {
    return annotatedRequirements;
  }

  private void buildAnnotatedRequirements(final FSM fsm) {
    annotatedRequirements = new ArrayList<AnnotatedRequirement>();
    danglingMethods = new ArrayDeque<Method>();

    fsm.getTransitions()
        .forEach(fsmTransition -> addStepAnnotatedRequirements(fsmTransition.getName().toString(),
            fsmTransition.getTransition().getModelObject()));

    if (!danglingMethods.isEmpty()) {
      throw new MissingRequirementStepException(danglingMethods.peek());
    }
  }

  private void addStepAnnotatedRequirements(final String step, final Object modelObject) {
    final Collection<AnnotatedRequirement> found = stream(modelObject.getClass().getMethods())
        .filter(method -> isRequirementForStep(step, method))
        .map(method -> createAnnotatedRequirement(step, method)).collect(toList());

    annotatedRequirements.addAll(found);
  }

  private boolean isRequirementForStep(final String step, final Method method) {
    final Annotation annotation = method.getAnnotation(requirementAnnotationClass);
    if (annotation != null) {
      if (method.getAnnotation(TestStep.class) != null) {
        // The requirement is on a test step method.
        return true;
      }

      danglingMethods.push(method);
      if (getStep(annotation).equalsIgnoreCase(step) || method.getName().endsWith(step)) {
        danglingMethods.pop();
        return true;
      }
    }

    return false;
  }

  private AnnotatedRequirement createAnnotatedRequirement(final String step, final Method method) {
    final Annotation annotation = method.getAnnotation(requirementAnnotationClass);
    final AnnotatedRequirement annotatedRequirement = new AnnotatedRequirement(
        requirementNamingStrategy, getValue(annotation), step, method.getName());

    return annotatedRequirement;
  }
}
