
package com.agilarity.osmo.example.vend.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import osmo.tester.annotation.Guard;
import osmo.tester.annotation.Post;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

import com.agilarity.osmo.example.vend.model.api.VendingmachineModel;
import com.agilarity.osmo.example.vend.uut.VendingMachine;
import com.agilarity.osmo.requirement.Requirement;

public class ReturnCoins extends VendingmachineModel {
  final static Logger LOG = LoggerFactory.getLogger(ReturnCoins.class);

  public ReturnCoins(final Requirements requirements, final VendingMachine vendingMachine) {
    super(requirements, vendingMachine);
  }

  @Guard
  public boolean guardReturnCoins() {
    return vendingMachine.getCash() > 0;
  }

  @TestStep
  public void returnCoins() {
    LOG.debug("Returning coins");
    vendingMachine.returnCoins();
  }

  @Post
  @Requirement
  public void shouldReturnCoins() {
    assertThat(vendingMachine.getCash()).isEqualTo(0);
  }
}
