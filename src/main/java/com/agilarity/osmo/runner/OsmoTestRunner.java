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

package com.agilarity.osmo.runner;

import java.util.Collection;

import osmo.tester.OSMOConfiguration;
import osmo.tester.OSMOTester;
import osmo.tester.model.Requirements;

@Deprecated
/**
 * Responsible for running OSMO tests.
 */
public class OsmoTestRunner {
  private static final int DEFAULT_SEED = 357;
  private final transient OSMOTester tester;

  /**
   * Configure runner.
   *
   * @param configuration Test configuration
   */
  public OsmoTestRunner(final OSMOConfiguration configuration) {
    super();
    tester = new OSMOTester();
    tester.setConfig(configuration);
  }

  /**
   * Generate the tests and assure every requirement is covered.
   */
  public void generateTest() {
    generateTest(DEFAULT_SEED);
    assertCoverage();
  }

  /**
   * Generate the tests and assure every requirement is covered.
   *
   * @param seed Value used to randomize test steps
   */
  public void generateTest(final int seed) {
    tester.generate(seed);
    assertCoverage();
  }

  private void assertCoverage() {
    final Requirements requirements = tester.getSuite().getRequirements();

    if (requirements != null) {
      final Collection<String> missingCoverage = requirements.getMissingCoverage();
      if (!missingCoverage.isEmpty()) {
        throw new OsmoTestException("Not covered " + missingCoverage.toString());
      }
    }
  }
}
