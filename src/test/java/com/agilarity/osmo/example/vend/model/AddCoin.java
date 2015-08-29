
package com.agilarity.osmo.example.vend.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import osmo.tester.annotation.Guard;
import osmo.tester.annotation.Post;
import osmo.tester.annotation.Pre;
import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;
import osmo.tester.model.data.ValueSet;

import com.agilarity.osmo.example.vend.model.api.VendingmachineModel;
import com.agilarity.osmo.example.vend.money.Coin;
import com.agilarity.osmo.example.vend.money.Dime;
import com.agilarity.osmo.example.vend.money.Nickle;
import com.agilarity.osmo.example.vend.money.Quarter;
import com.agilarity.osmo.example.vend.uut.VendingMachine;
import com.agilarity.osmo.requirement.Requirement;

public class AddCoin extends VendingmachineModel {

  public AddCoin(final Requirements requirements, final VendingMachine vendingMachine) {
    super(requirements, vendingMachine);
  }

  final static Logger LOG = LoggerFactory.getLogger(AddCoin.class);

  private final ValueSet<Coin> coins = new ValueSet<Coin>(new Quarter(), new Dime(), new Nickle());
  private Coin coin;
  private int cashBeforeAdd;

  @Guard
  public boolean guardAddCoin() {
    return !vendingMachine.isEmpty();
  }

  @Pre
  public void beforeAddCoin() {
    coin = coins.random();
    cashBeforeAdd = vendingMachine.getCash();
  }

  @TestStep
  public void addCoin() {
    LOG.debug("Adding {} cents", coin.value());
    vendingMachine.add(coin);
  }

  @Post
  @Requirement
  public void shouldAddCoin() {
    final int expected = cashBeforeAdd + coin.value();
    assertThat(vendingMachine.getCash()).isEqualTo(expected);
  }
}
