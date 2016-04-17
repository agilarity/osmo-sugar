
package com.agilarity.osmo.requirement.internal; // NOPMD - Static imports are fine

import static com.agilarity.osmo.requirement.internal.AnnotationAccesor.getStep;
import static com.agilarity.osmo.requirement.internal.AnnotationAccesor.getValue;
import static java.lang.Character.toUpperCase;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
  private final transient Collection<AnnotatedRequirement> annotatedRequirements;

  /**
   * Create builder.
   */
  public AnnotationRequirementsBuilder(final FSM fsm,
      final RequirementNamingStrategy requirementNamingStrategy,
      final Class<? extends Annotation> requirementAnnotationClass) {
    super();
    this.requirementNamingStrategy = requirementNamingStrategy;
    this.requirementAnnotationClass = requirementAnnotationClass;
    annotatedRequirements = new ArrayList<AnnotatedRequirement>();
    buildAnnotatedRequirements(fsm);
  }

  /**
   * @return the annotatedRequirements.
   */
  public Collection<AnnotatedRequirement> getAnnotatedRequirements() {
    return annotatedRequirements;
  }

  private void buildAnnotatedRequirements(final FSM fsm) {
    final List<Method> requirementMethods = fetchRequirementMethods(fsm);
    requirementMethods.forEach(method -> makeAnnotatedRequirements(fetchSteps(fsm), method));
  }

  private void makeAnnotatedRequirements(final Collection<String> steps, final Method method) {
    final String id = getValue(method.getAnnotation(requirementAnnotationClass));
    getStepsForRequirement(steps, method).forEach(
        step -> addAnnotatedRequirementForStep(id, capitalize(step), method.getName()));
  }

  private List<String> getStepsForRequirement(final Collection<String> steps, final Method method) {
    final Optional<String> stepName = ofNullable(getStepName(method));
    if (stepName.isPresent()) {
      return asList(stepName.get());
    } else {
      final Optional<String> requirementStep = ofNullable(getRequirementStep(method));
      if (requirementStep.isPresent()) {
        return asList(requirementStep.get());
      } else {
        final Optional<TestStep> testStepAnnotation = ofNullable(method
            .getAnnotation(TestStep.class));
        if (testStepAnnotation.isPresent()) {
          return asList(method.getName());
        } else {
          final Optional<String> stepFromMethod = ofNullable(getStepFromMethod(steps, method));
          if (stepFromMethod.isPresent()) {
            return asList(stepFromMethod.get());
          }
        }
      }
    }

    throw new MissingRequirementStepException(method);
  }

  private String getStepName(final Method method) {
    final Optional<TestStep> testStepAnnotation = ofNullable(method.getAnnotation(TestStep.class));
    if (testStepAnnotation.isPresent()) {
      final String stepName = testStepAnnotation.get().name();
      if (stepName.isEmpty()) {
        return null;
      } else {
        return stepName;
      }
    } else {
      return null;
    }
  }

  private String getRequirementStep(final Method method) {
    final String requirementStep = getStep(method.getAnnotation(requirementAnnotationClass));
    if (requirementStep.isEmpty()) {
      return null;
    } else {
      return requirementStep;
    }
  }

  private String getStepFromMethod(final Collection<String> steps, final Method method) { // NOPMD
    for (final String step : steps) {
      if (method.getName().endsWith(step)) {
        return step;
      }
    }

    return null;
  }

  private void addAnnotatedRequirementForStep(final String id, final String step,
      final String method) {
    annotatedRequirements
        .add(new AnnotatedRequirement(requirementNamingStrategy, id, step, method));
  }

  private List<Method> fetchRequirementMethods(final FSM fsm) {
    return fsm
        .getTransitions()
        .stream()
        .flatMap(
            fsmTransition -> stream(fsmTransition.getTransition().getModelObject().getClass()
                .getMethods()))
        .filter(method -> method.getAnnotation(requirementAnnotationClass) != null)
        .collect(toList());
  }

  private List<String> fetchSteps(final FSM fsm) {
    return fsm.getTransitions().stream().map(fsmTransition -> fsmTransition.getName().toString())
        .collect(toList());
  }

  private String capitalize(final String word) {
    return toUpperCase(word.charAt(0)) + word.substring(1);
  }
}
