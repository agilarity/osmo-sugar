
package com.agilarity.osmo.example.vend.uut;

import com.agilarity.osmo.example.vend.money.Coin;

public interface VendingMachine {
  public void fill();

  public boolean isEmpty();

  public void add(Coin coin);

  public void returnCoins();

  public boolean isDispensible();

  public void dispense();

  public int getItems();

  public int getCash();

  public int getPrice();
}
