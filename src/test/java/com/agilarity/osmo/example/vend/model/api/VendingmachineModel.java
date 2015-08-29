
package com.agilarity.osmo.example.vend.model.api;

import osmo.tester.model.Requirements;

import com.agilarity.osmo.example.vend.uut.VendingMachine;

public class VendingmachineModel {
  // The requirements are used by RequirementAnnotationListener, not the model directly.
  @SuppressWarnings("unused")
  private final Requirements requirements;
  protected final VendingMachine vendingMachine;

  public VendingmachineModel(final Requirements requirements, final VendingMachine vendingMachine) {
    super();
    this.requirements = requirements;
    this.vendingMachine = vendingMachine;
  }
}
