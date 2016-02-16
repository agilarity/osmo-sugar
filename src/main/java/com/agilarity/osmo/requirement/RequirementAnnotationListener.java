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

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.agilarity.osmo.requirement.name.RequirementNamingStrategy;
import com.agilarity.osmo.requirement.name.SimpleRequirementNamingStrategy;

import osmo.tester.OSMOConfiguration;
import osmo.tester.generator.listener.AbstractListener;
import osmo.tester.generator.testsuite.TestCase;
import osmo.tester.generator.testsuite.TestCaseStep;
import osmo.tester.model.FSM;
import osmo.tester.model.Requirements;

/**
 * Add requirements from the test model and cover them on success.
 */
public class RequirementAnnotationListener extends AbstractListener {
  private final transient RequirementNamingStrategy requirementNamingStrategy;
  private transient Requirements requirements;
  private transient ConcurrentMap<String, Collection<AnnotatedRequirement>> stepRequirements;
  private transient Deque<Method> danglingMethods;
  private transient AnnotatedRequirement failingRequirement;
  private transient Collection<AnnotatedRequirement> passingRequirements;

  /**
   * Use the @{code RequirementNamingStrategy} by default.
   */
  public RequirementAnnotationListener() {
    this(new SimpleRequirementNamingStrategy());
  }

  /**
   * Inject the {@code RequirementNamingStrategy}.
   */
  public RequirementAnnotationListener(final RequirementNamingStrategy requirementNamingStrategy) {
    super();
    this.requirementNamingStrategy = requirementNamingStrategy;
  }

  /**
   * @return the failingRequirement.
   */
  public AnnotatedRequirement getFailingRequirement() {
    return failingRequirement;
  }

  /**
   * @return the passingRequirements.
   */
  public Collection<AnnotatedRequirement> getPassingRequirements() {
    return passingRequirements;
  }

  /**
   * Register the requirements for a step.
   */
  @Override
  public void init(final long seed, final FSM fsm, final OSMOConfiguration config) {
    requirements = fsm.getRequirements();
    if (requirements == null) {
      throw new MissingRequirementsObjectException();
    }

    passingRequirements = new ArrayList<AnnotatedRequirement>();
    stepRequirements = new ConcurrentHashMap<String, Collection<AnnotatedRequirement>>();
    danglingMethods = new ArrayDeque<Method>();

    fsm.getTransitions().forEach(
        fsmTransition -> addStepAnnotatedRequirements(fsmTransition.getName().toString(),
            fsmTransition.getTransition().getModelObject()));

    if (!danglingMethods.isEmpty()) {
      throw new MissingRequirementStepException(danglingMethods.peek());
    }
  }

  private void addStepAnnotatedRequirements(final String step, final Object modelObject) {
    final List<AnnotatedRequirement> list = stream(modelObject.getClass().getMethods())
        .filter(method -> isRequirementForStep(step, method))
        .map(method -> createAnnotatedRequirement(step, method)).collect(toList());

    list.forEach(requirement -> requirements.add(requirement.getName()));
    stepRequirements.put(step, list);
  }

  private boolean isRequirementForStep(final String step, final Method method) {
    if (method.getAnnotation(Requirement.class) != null) {
      danglingMethods.push(method);
      if (method.getAnnotation(Requirement.class).step().equalsIgnoreCase(step)
          || method.getName().endsWith(step)) {
        danglingMethods.pop();
        return true;
      }
    }

    return false;
  }

  private AnnotatedRequirement createAnnotatedRequirement(final String step, final Method method) {
    final AnnotatedRequirement annotatedRequirement = new AnnotatedRequirement(
        requirementNamingStrategy, method.getAnnotation(Requirement.class).value(), step,
        method.getName());

    return annotatedRequirement;
  }

  /**
   * Cover the requirements because the step did not fail.
   */
  @Override
  public void stepDone(final TestCaseStep step) {
    final Collection<AnnotatedRequirement> annotatedRequirements = stepRequirements.get(step
        .getName());
    if (annotatedRequirements != null) {
      annotatedRequirements.forEach(requirement -> passRequirement(requirement));
    }
  }

  private void passRequirement(final AnnotatedRequirement requirement) {
    requirements.covered(requirement.getName());
    passingRequirements.add(requirement);
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
    final List<String> list = stepRequirements.get(step).stream()
        .map(requirement -> requirement.getName()).collect(toList());
    requirements.getFullCoverage().removeAll(list);
  }

  private boolean uncoverFailingRequirement(final String step, final Throwable error) {
    final String method = getThrowingMethod(error);
    final Optional<AnnotatedRequirement> sourceOfError = stepRequirements.get(step).stream()
        .filter(requirement -> requirement.getMethod().equals(method)).findFirst();

    if (sourceOfError.isPresent()) {
      failingRequirement = sourceOfError.get();
      final List<String> nameAsList = Arrays.asList(failingRequirement.getName());

      // Use removeAll instead of remove to assure every reference to the name is removed.
      requirements.getFullCoverage().removeAll(nameAsList);

      return true;
    }

    return false;
  }

  private String getThrowingMethod(final Throwable error) {
    final StackTraceElement throwingMethod = error.getStackTrace()[0];
    return throwingMethod.getMethodName();
  }
}
