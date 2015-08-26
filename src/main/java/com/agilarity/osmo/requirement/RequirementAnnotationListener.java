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
	private ConcurrentMap<String, Collection<String>> stepRequirements;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(final long seed, final FSM fsm, final OSMOConfiguration config) {
		requirements = fsm.getRequirements();
		stepRequirements = new ConcurrentHashMap<String, Collection<String>>();
		loadStepRequirements(fsm);
		stepRequirements.forEach((k, v) -> addRequirements(v));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void step(final TestCaseStep step) {
		final Collection<String> requirementNames = stepRequirements.get(step.getName());
		if (requirementNames != null) {
			requirementNames.forEach(n -> requirements.covered(n));
		}
	}

	private void loadStepRequirements(final FSM fsm) {
		final Collection<FSMTransition> transitions = fsm.getTransitions();
		for (final FSMTransition fsmTransition : transitions) {
			final Object modelObject = fsmTransition.getTransition().getModelObject();
			final String modelObjectName = modelObject.getClass().getSimpleName();
			final Collection<String> reqs = fetchRequirementNames(modelObject, modelObjectName);
			stepRequirements.put(modelObjectName, reqs);
		}
	}

	private Collection<String> fetchRequirementNames(final Object modelObject,
			final String modelObjectName) {

		return asList(modelObject.getClass().getMethods()).stream()
				.filter(m -> m.getAnnotation(Requirement.class) != null)
				.map(m -> buildRequirement(modelObjectName, m.getName())).collect(toList());
	}

	private String buildRequirement(final String modelObjectName, final String name) {
		return format("%s.%s", modelObjectName, name);
	}

	private void addRequirements(final Collection<String> requirementNames) {
		requirementNames.forEach(r -> requirements.add(r));
	}
}
