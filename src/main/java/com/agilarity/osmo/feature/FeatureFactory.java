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

import osmo.tester.model.ModelFactory;
import osmo.tester.model.Requirements;

/**
 * Responsible for defining a feature model factory.
 *
 * @param D Test driver is the facade to the system under test
 * @param S Test state is used to track the state of the system under test
 */
public abstract class FeatureFactory<D, S> implements ModelFactory { // NOPMD

  protected final Requirements requirements; // NOCS NOPMD
  protected final D driver; // NOCS NOPMD
  protected final S state; // NOCS NOPMD

  /**
   * Initialize fields.
   *
   * @param requirements the Requirements
   * @param driver the System driver
   * @param state the Test state
   */
  public FeatureFactory(final Requirements requirements, final D driver, final S state) {
    super();
    this.requirements = requirements;
    this.driver = driver;
    this.state = state;
  }
}
