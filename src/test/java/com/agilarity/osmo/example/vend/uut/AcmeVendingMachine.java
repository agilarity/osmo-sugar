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

package com.agilarity.osmo.example.vend.uut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilarity.osmo.example.vend.money.Coin;

public class AcmeVendingMachine implements VendingMachine {
  private static final Logger LOG = LoggerFactory.getLogger(AcmeVendingMachine.class);
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
