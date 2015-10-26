/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Joseph A. Cruz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
import osmo.tester.generator.testsuite.TestCase;
import osmo.tester.generator.testsuite.TestCaseStep;
import osmo.tester.model.FSM;
import osmo.tester.model.Requirements;

/**
 * Add requirements from the test model and cover them on success.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class RequirementAnnotationListener extends AbstractListener {
  private transient Requirements requirements;
  private transient ConcurrentMap<String, Collection<String>> stepRequirements;

  /**
   * Register the requirements for a step.
   */
  @Override
  public void init(final long seed, final FSM fsm, final OSMOConfiguration config) {
    requirements = fsm.getRequirements();
    if (requirements == null) {
      throw new MissingRequirementsObjectException();
    }

    stepRequirements = new ConcurrentHashMap<String, Collection<String>>();
    extractRequirements(fsm);
    stepRequirements.forEach((stepName, name) -> addRequirements(name));
  }

  /**
   * Cover the requirements because the step did not fail.
   */
  @SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
  @Override
  public void step(final TestCaseStep step) {
    final Collection<String> requirementNames = stepRequirements.get(step.getName());
    if (requirementNames != null) {
      requirementNames.forEach(requirementName -> requirements.covered(requirementName));
    }
  }

  /**
   * Uncover the failing requirement when known or all steps requirements otherwise.
   */
  @Override
  public void testError(final TestCase test, final Throwable error) {
    final String step = test.getCurrentStep().getName();

    if (!uncoverFailingRequirement(step, error)) {
      uncoverAllStepRequirements(step);
    }
  }

  private void uncoverAllStepRequirements(final String step) {
    requirements.getFullCoverage().removeAll(stepRequirements.get(step));
  }

  private boolean uncoverFailingRequirement(final String step, final Throwable error) {
    final String requirement = buildRequirementFromError(step, error);
    final Collection<String> requirementNames = stepRequirements.get(step);

    if (requirementNames.contains(requirement)) {
      requirements.getFullCoverage().removeAll(asList(requirement));
      return true;
    }

    return false;
  }

  private String buildRequirementFromError(final String step, final Throwable error) {
    final StackTraceElement throwingMethod = error.getStackTrace()[0];
    final String method = throwingMethod.getMethodName();
    return buildRequirement(method, step);
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
    final String value = annotation.step();

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
