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

package com.agilarity.osmo.feature;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import osmo.tester.annotation.TestStep;
import osmo.tester.model.Requirements;

import com.agilarity.osmo.feature.models.AbstractTestFeature;

public class AddRequirementTest {
  private Requirements requirements;

  @BeforeTest
  public void beforeTest() {
    requirements = new Requirements();
  }

  @Test
  public void shouldAddRequirement() {
    final SimpleFeature feature = createFeature();

    assertThat(feature.getRequirements().getRequirements().contains(feature.getRequirement()))
        .isTrue();
  }

  @Test
  public void shouldOnlyAddRequirementOnce() {
    createFeature();
    createFeature();
  }

  public class SimpleFeature extends AbstractTestFeature {

    public SimpleFeature(final Requirements requirements) {
      super(requirements);
    }

    @TestStep()
    public void doNothing() {
    }
  }

  private SimpleFeature createFeature() {
    return new SimpleFeature(requirements);
  }
}
