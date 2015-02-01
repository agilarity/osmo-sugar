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

import static com.agilarity.osmo.runner.ConfigurationBuilder.createConfiguration;

import org.testng.annotations.Test;

import osmo.tester.OSMOConfiguration;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

public class CoverRequirementTest {
	@Test
	public void shouldRequireCoverage() {
		final CoverRequirement model = new CoverRequirement(new Requirements());
		final OSMOConfiguration configuration = createConfiguration(model);
		final OsmoTestRunner runner = new OsmoTestRunner(configuration);
		runner.generateTest();
	}

	public class CoverRequirement {
		private final Requirements requirements;

		public CoverRequirement(final Requirements requirements) {
			super();
			this.requirements = requirements;
			this.requirements.add("R1");
		}

		@TestStep
		public void coverRequirements() {
			requirements.covered("R1");
		}
	}
}
