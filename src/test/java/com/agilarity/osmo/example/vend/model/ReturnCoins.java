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
