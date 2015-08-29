
package com.agilarity.osmo.requirement;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import osmo.tester.OSMOConfiguration;
import osmo.tester.generator.listener.AbstractListener;
import osmo.tester.generator.testsuite.TestCaseStep;
import osmo.tester.model.FSM;
import osmo.tester.model.FSMTransition;
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
    loadStepRequirements(fsm);
    stepRequirements.forEach((modelObjectName, name) -> addRequirements(name));
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

  private void loadStepRequirements(final FSM fsm) {
    fsm.getTransitions().forEach(
        fsmTransition -> stepRequirements.put(
            getModelObjectName(getModelObject(fsmTransition)),
            fetchRequirementNames(getModelObject(fsmTransition),
                getModelObjectName(getModelObject(fsmTransition)))));
  }

  private String getModelObjectName(final Object modelObject) {
    return modelObject.getClass().getSimpleName();
  }

  private Object getModelObject(final FSMTransition fsmTransition) {
    return fsmTransition.getTransition().getModelObject();
  }

  private Collection<String> fetchRequirementNames(final Object modelObject,
      final String modelObjectName) {

    return asList(modelObject.getClass().getMethods()).stream()
        .filter(method -> method.getAnnotation(Requirement.class) != null)
        .map(method -> buildRequirement(modelObjectName, method.getName())).collect(toList());
  }

  private String buildRequirement(final String modelObjectName, final String requirementName) {
    return format("%s.%s", modelObjectName, requirementName);
  }

  private void addRequirements(final Collection<String> requirementNames) {
    requirementNames.forEach(requirementName -> requirements.add(requirementName));
  }
}
