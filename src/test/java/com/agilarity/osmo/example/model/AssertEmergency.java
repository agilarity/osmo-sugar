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

package com.agilarity.osmo.example.model;

import static com.agilarity.osmo.example.detector.SafetyStatus.EMERGENCY;
import static org.assertj.core.api.Assertions.assertThat;
import osmo.tester.annotation.Guard;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

import com.agilarity.osmo.example.SmokeDetectorState;
import com.agilarity.osmo.example.detector.SmokeDetector;
import com.agilarity.osmo.feature.Feature;

public class AssertEmergency extends Feature<SmokeDetector, SmokeDetectorState> {

  public AssertEmergency(final Requirements requirements, final SmokeDetector driver,
      final SmokeDetectorState state) {
    super(requirements, driver, state);
  }

  @Guard
  public boolean guardDetectEmergencyStatus() {
    return state.getLevel() > 14;
  }

  @TestStep
  public void detectEmergencyStatus() {
    assertThat(driver.detect(state.getLevel())).isEqualTo(EMERGENCY);
    coverRequirement();
  }
}
