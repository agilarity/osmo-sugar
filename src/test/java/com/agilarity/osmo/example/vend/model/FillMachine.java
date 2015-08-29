
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

public class FillMachine extends VendingmachineModel {
  final static Logger LOG = LoggerFactory.getLogger(FillMachine.class);

  public FillMachine(final Requirements requirements, final VendingMachine vendingMachine) {
    super(requirements, vendingMachine);
  }

  @Guard
  public boolean guardFillMachine() {
    return vendingMachine.isEmpty();
  }

  @TestStep
  public void fillMachine() {
    LOG.debug("Filling machine");
    vendingMachine.fill();
  }

  @Post
  @Requirement
  public void shouldFillMachine() {
    assertThat(vendingMachine.isEmpty()).isFalse();
  }
}
