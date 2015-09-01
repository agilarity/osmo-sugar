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

package com.agilarity.osmo.example.vend;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import osmo.tester.OSMOConfiguration;
import osmo.tester.generator.endcondition.Length;
import osmo.tester.generator.listener.TracePrinter;
import osmo.tester.model.Requirements;

import com.agilarity.osmo.example.vend.uut.AcmeVendingMachine;
import com.agilarity.osmo.example.vend.uut.VendingMachine;
import com.agilarity.osmo.requirement.RequirementAnnotationListener;
import com.agilarity.osmo.tester.RequirementEnforcingOsmoTester;

public class VendingMachineOsmoTest {
  private Requirements requirements;
  private VendingMachine vendingMachine;
  private RequirementEnforcingOsmoTester tester;

  @BeforeSuite
  public void beforeSuite() {
    tester = new RequirementEnforcingOsmoTester();
    requirements = new Requirements();
    vendingMachine = new AcmeVendingMachine();
  }

  @Test
  public void shouldVerifyVendingMachine() {
    tester.setConfig(createConfiguration());
    tester.generate(1);
  }

  private OSMOConfiguration createConfiguration() {
    final OSMOConfiguration config = new OSMOConfiguration();
    config.setFactory(createFactory());
    config.addListener(new TracePrinter());
    config.addListener(new RequirementAnnotationListener());
    config.setTestEndCondition(new Length(25));
    config.setSuiteEndCondition(new Length(10));
    return config;
  }

  private VendingMachineModelFactory createFactory() {
    return new VendingMachineModelFactory(requirements, vendingMachine);
  }
}
