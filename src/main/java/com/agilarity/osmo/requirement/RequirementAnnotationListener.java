package com.agilarity.osmo.requirement;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import osmo.tester.OSMOConfiguration;
import osmo.tester.generator.listener.AbstractListener;
import osmo.tester.generator.testsuite.TestCaseStep;
import osmo.tester.model.FSM;
import osmo.tester.model.Requirements;

/**
 * Add requirements from the test model and cover them on success.
 */
public class RequirementAnnotationListener extends AbstractListener {
  private transient Requirements requirements;
  private transient ConcurrentMap<String, Collection<String>> stepRequirements;

  /**
   * Register the requirements for a step. {@inheritDoc}
   */
  @Override
  public void init(final long seed, final FSM fsm, final OSMOConfiguration config) {
    requirements = fsm.getRequirements();
    stepRequirements = new ConcurrentHashMap<String, Collection<String>>();
    extractRequirements(fsm);
    stepRequirements.forEach((stepName, name) -> addRequirements(name));
  }

  /**
   * Cover the requirements because the step did not fail. {@inheritDoc}
   */
  @Override
  public void step(final TestCaseStep step) {
    final Collection<String> requirementNames = stepRequirements.get(step.getName());
    if (requirementNames != null) {
      requirementNames.forEach(requirementName -> requirements.covered(requirementName));
    }
  }

  private void extractRequirements(final FSM fsm) {
    fsm.getTransitions().forEach(
        transition -> stepRequirements.put(
            transition.getName().toString(),
            fetchRequirementNames(transition.getTransition().getModelObject(), transition.getName()
                .toString())));
  }

  private Collection<String> fetchRequirementNames(final Object model, final String step) {
    return findAnnotatedMethods(model).stream().filter(method -> assertStep(method, step))
        .map(method -> buildRequirement(method.getName(), step)).collect(toList());
  }

  private Collection<Method> findAnnotatedMethods(final Object model) {
    return asList(model.getClass().getMethods()).stream()
        .filter(method -> method.getAnnotation(Requirement.class) != null).collect(toList());
  }

  private boolean assertStep(final Method method, final String step) {
    final Requirement annotation = method.getAnnotation(Requirement.class);
    final String value = annotation.value();

    if (value.equalsIgnoreCase(step) || method.getName().endsWith(step)) {
      return true;
    } else {
      throw new MissingRequirementStepException(method, step);
    }
  }

  private String buildRequirement(final String method, final String step) {
    return format("%s.%s", step, method);
  }

  private void addRequirements(final Collection<String> requirementNames) {
    requirementNames.forEach(requirementName -> requirements.add(requirementName));
  }
}
