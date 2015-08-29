
package com.agilarity.osmo.example.vend.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import osmo.tester.annotation.Guard;
import osmo.tester.annotation.Post;
import osmo.tester.annotation.Pre;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

import com.agilarity.osmo.example.vend.model.api.VendingmachineModel;
import com.agilarity.osmo.example.vend.uut.VendingMachine;
import com.agilarity.osmo.requirement.Requirement;

public class DispenseItem extends VendingmachineModel {
  final static Logger LOG = LoggerFactory.getLogger(DispenseItem.class);
  private int itemsBeforeDispense;
  private int cashBeforeDispense;

  public DispenseItem(final Requirements requirements, final VendingMachine vendingMachine) {
    super(requirements, vendingMachine);
  }

  @Guard
  public boolean guardDispenseItem() {
    return vendingMachine.isDispensible();
  }

  @Pre
  public void beforeDispenseItem() {
    itemsBeforeDispense = vendingMachine.getItems();
    cashBeforeDispense = vendingMachine.getCash();
  }

  @TestStep
  public void dispenseItem() {
    LOG.debug("Dispensing item");
    vendingMachine.dispense();
  }

  @Post
  public void shouldDispenseItem() {
    shouldReductInventory();
    shouldSpendCash();
  }

  @Requirement
  public void shouldSpendCash() {
    assertThat(vendingMachine.getCash()).isEqualTo(cashBeforeDispense - vendingMachine.getPrice());
  }

  @Requirement
  public void shouldReductInventory() {
    assertThat(vendingMachine.getItems()).isEqualTo(--itemsBeforeDispense);
  }
}
