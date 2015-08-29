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

package com.agilarity.osmo.feature;

import static java.lang.String.format;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

import com.agilarity.osmo.runner.OsmoTestException;

/**
 * Responsible for defining a feature model.
 *
 * @param D Test driver is the facade to the system under test
 * @param S Test state is used to track the state of the system under test
 */
public abstract class Feature<D, S> { // NOPMD
  private static final int EXPECTED_STEPS = 1;
  private static final String ONE_STEP = "Expected [1] @TestStep annotation in %s but got [%s]"; // NOCS
  private final String requirement;
  protected final Requirements requirements; // NOCS NOPMD
  protected final D driver; // NOCS NOPMD
  protected final S state; // NOCS NOPMD

  /**
   * Initialize feature fields.
   *
   * @param requirements The requirements
   * @param driver Test driver is the facade to the system under test
   * @param state Test state is used to track the state of the system under test
   */
  public Feature(final Requirements requirements, final D driver, final S state) {
    this.requirements = requirements;
    this.driver = driver;
    this.state = state;

    requirement = findStepName();

    if (!this.requirements.getRequirements().contains(requirement)) {
      requirements.add(requirement);
    }
  }

  /**
   * Get OSMO requirements objects.
   *
   * @return Requirements
   */
  public Requirements getRequirements() {
    return requirements;
  }

  /**
   * Get the requirement name.
   * 
   * @return Requirement name
   */
  public String getRequirement() {
    return requirement;
  }

  /**
   * Cover the requirement for this feature.
   */
  protected void coverRequirement() {
    requirements.covered(requirement);
  }

  private String findStepName() {
    final Method method = findTestStepMethod();
    final TestStep annotation = method.getAnnotation(TestStep.class);
    if (annotation.value().isEmpty()) {
      return method.getName();
    } else {
      return annotation.value();
    }
  }

  private Method findTestStepMethod() {
    final List<Method> testSteps = findTestSteps();
    requireExactlyOneTestStep(testSteps);
    return testSteps.get(0);
  }

  private List<Method> findTestSteps() { // NOPMD
    final List<Method> testSteps = new ArrayList<Method>();
    final Method[] methods = getClass().getDeclaredMethods();
    for (final Method method : methods) {
      final TestStep annotation = method.getAnnotation(TestStep.class);
      if (annotation != null) {
        testSteps.add(method);
      }
    }
    return testSteps;
  }

  private void requireExactlyOneTestStep(final List<Method> testSteps) {
    if (testSteps.size() != EXPECTED_STEPS) {
      final String error = format(ONE_STEP, getClass().getSimpleName(), testSteps.size());
      throw new OsmoTestException(error);
    }
  }
}
