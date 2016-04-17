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

package com.agilarity.osmo.requirement.internal.locator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.agilarity.osmo.requirement.errors.MissingRequirementStepException;

/**
 * Provides step names for the requirement.
 */
public class RequirementStepLocator implements StepLocator {
  private final transient Method method;
  private final transient Collection<String> steps;
  private final transient Class<? extends Annotation> requirementAnnotationClass;
  private final transient List<StepLocator> locators;

  /**
   * Create step locator.
   */
  public RequirementStepLocator(final Method method, final Collection<String> steps,
      final Class<? extends Annotation> requirementAnnotationClass) {
    super();
    this.method = method;
    this.steps = steps;
    this.requirementAnnotationClass = requirementAnnotationClass;
    locators = new ArrayList<StepLocator>();
    addLocators();
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<String> getSteps() { // NOPMD - Data flow is fine
    for (final StepLocator locator : locators) {
      if (hasSteps(locator)) {
        return locator.getSteps();
      }
    }

    throw new MissingRequirementStepException(method);
  }

  private boolean hasSteps(final StepLocator locator) {
    return !locator.getSteps().isEmpty();
  }

  private void addLocators() {
    locators.add(new TestStepAnnotationStepLocator(method));
    locators.add(new RequirementAnnotationStepLocator(method, requirementAnnotationClass));
    locators.add(new TestStepMethodStepLocator(method));
    locators.add(new RequirementMethodStepLocator(method, steps));
  }
}
