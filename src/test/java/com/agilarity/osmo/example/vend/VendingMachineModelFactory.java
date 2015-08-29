
package com.agilarity.osmo.example.vend;

import osmo.tester.model.ModelFactory;
import osmo.tester.model.Requirements;
import osmo.tester.model.TestModels;

import com.agilarity.osmo.example.vend.model.AddCoin;
import com.agilarity.osmo.example.vend.model.DispenseItem;
import com.agilarity.osmo.example.vend.model.FillMachine;
import com.agilarity.osmo.example.vend.model.ReturnCoins;
import com.agilarity.osmo.example.vend.uut.VendingMachine;

public class VendingMachineModelFactory implements ModelFactory {
  private final Requirements requirements;
  private final VendingMachine vendingMachine;

  public VendingMachineModelFactory(final Requirements requirements,
      final VendingMachine vendingMachine) {
    super();
    this.requirements = requirements;
    this.vendingMachine = vendingMachine;
  }

  @Override
  public void createModelObjects(final TestModels models) {
    models.add(new FillMachine(requirements, vendingMachine));
    models.add(new AddCoin(requirements, vendingMachine));
    models.add(new DispenseItem(requirements, vendingMachine));
    models.add(new ReturnCoins(requirements, vendingMachine));
  }
}
