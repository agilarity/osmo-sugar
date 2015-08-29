
package com.agilarity.osmo.example.vend.uut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilarity.osmo.example.vend.money.Coin;

public class AcmeVendingMachine implements VendingMachine {
  final static Logger LOG = LoggerFactory.getLogger(AcmeVendingMachine.class);
  private static final int COST = 40;
  private int items;
  private int cash;

  @Override
  public void fill() {
    items = 10;
    LOG.debug("Filled machine with {} items", items);
  }

  @Override
  public boolean isEmpty() {
    return items == 0;
  }

  @Override
  public void add(final Coin coin) {
    cash += coin.value();
    LOG.debug("Added {} cents for a total of {} cents", coin.value(), cash);
  }

  @Override
  public boolean isDispensible() {
    return cash >= COST;
  }

  @Override
  public void dispense() {
    if (isDispensible()) {
      --items;
      LOG.debug("Dispensed 1 item with {} items remaining", items);
      spendCash();
    }
  }

  private void spendCash() {
    cash -= COST;
    LOG.debug("Spent {} cents with {} cents remaining", COST, cash);
  }

  @Override
  public int getItems() {
    return items;
  }

  @Override
  public int getCash() {
    return cash;
  }

  @Override
  public int getPrice() {
    return COST;
  }

  @Override
  public void returnCoins() {
    cash = 0;
    LOG.debug("Returned coins, {} cents remaining", cash);
  }
}
