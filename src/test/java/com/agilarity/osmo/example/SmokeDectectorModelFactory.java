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

package com.agilarity.osmo.example;

import osmo.tester.model.Requirements;
import osmo.tester.model.TestModels;

import com.agilarity.osmo.example.detector.SmokeDetector;
import com.agilarity.osmo.example.model.AddSmoke;
import com.agilarity.osmo.example.model.AssertEmergency;
import com.agilarity.osmo.example.model.AssertSafe;
import com.agilarity.osmo.example.model.AssertWarning;
import com.agilarity.osmo.example.model.RemoveSmoke;
import com.agilarity.osmo.feature.FeatureFactory;

public class SmokeDectectorModelFactory extends FeatureFactory<SmokeDetector, SmokeDetectorState> {

  public SmokeDectectorModelFactory(final Requirements requirements, final SmokeDetector driver,
      final SmokeDetectorState state) {
    super(requirements, driver, state);
  }

  @Override
  public void createModelObjects(final TestModels testModels) {
    testModels.add(new AssertEmergency(requirements, driver, state));
    testModels.add(new AssertSafe(requirements, driver, state));
    testModels.add(new AssertWarning(requirements, driver, state));
    testModels.add(new RemoveSmoke(state));
    testModels.add(new AddSmoke(state));
  }
}
