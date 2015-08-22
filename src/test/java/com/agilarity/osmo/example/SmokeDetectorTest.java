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

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import osmo.tester.OSMOConfiguration;
import osmo.tester.generator.endcondition.Length;
import osmo.tester.model.Requirements;

import com.agilarity.osmo.example.detector.SmokeDetector;
import com.agilarity.osmo.runner.OsmoTestRunner;

public class SmokeDetectorTest {

	private OsmoTestRunner runner;
	private Requirements requirements;
	private OSMOConfiguration configuration;

	@BeforeTest public void beforeTest() {
		configuration = new OSMOConfiguration();
		configuration.setSuiteEndCondition(new Length(1));
		configuration.setTestEndCondition(new Length(400));

		requirements = new Requirements();
		configuration.setFactory(new SmokeDectectorModelFactory(requirements, new SmokeDetector(),
				new SmokeDetectorState()));
		runner = new OsmoTestRunner(configuration);
	}

	@Test public void shouldDetectSmokeLevels() {
		runner.generateTest();
	}
}
