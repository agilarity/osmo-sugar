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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import osmo.tester.annotation.TestStep;

/**
 * Provides step name from the test step method.
 */
public class TestStepMethodStepLocator implements StepLocator {
  private final transient Method method;

  /**
   * Create step locator.
   */
  public TestStepMethodStepLocator(final Method method) {
    super();
    this.method = method;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<String> getSteps() {
    final Optional<TestStep> testStepAnnotation = ofNullable(method.getAnnotation(TestStep.class));
    if (testStepAnnotation.isPresent()) {
      return asList(method.getName());
    } else {
      return emptyList();
    }
  }
}
