
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
