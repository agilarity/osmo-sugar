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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Provides step name from the requirements method.
 */
public class RequirementMethodStepLocator implements StepLocator {
  private final transient Method method;
  private final transient Collection<String> steps;

  /**
   * Create step locator.
   */
  public RequirementMethodStepLocator(final Method method, final Collection<String> steps) {
    super();
    this.method = method;
    this.steps = steps;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public List<String> getSteps() { // NOPMD - Data flow is fine.
    for (final String step : steps) {
      if (method.getName().endsWith(step)) {
        return asList(step);
      }
    }

    return emptyList();
  }
}
