/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.vas.domain.repository.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.testng.annotations.Test;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.AddressService;
import org.vas.domain.repository.exception.AddressNotFoundException;

public class TestAddressService extends AbstractAddressTest {

  @Inject
  AddressService addressService;

  @Test
  public void itShouldHaveService() {
    assertThat(addressService).as("The address service shouldn't be null").isNotNull();
  }

  @Test
  public void itShouldCreateUnPersistedAddress() {
    String label = "Label";
    float lat = 48F;
    float lng = 1F;

    Address address = addressService.create(label, lat, lng);

    assertThat(address.label).as("The address label should be equals to '" + label + "'").isEqualTo(label);
    assertThat(address.latitude).as("The address latitude should be equals to '" + lat + "'").isEqualTo(lat);
    assertThat(address.longitude).as("The address longitude should be equals to '" + lng + "'").isEqualTo(lng);
  }

  @Test
  public void itShouldSaveAndReadAddress() {
    String label = "Label";
    Address address = addressService.create(label, 48F, 1F);

    addressService.save(address);
    assertThat(address.id).as("The address should have a positive id").isGreaterThan(0);

    address = addressService.fetch(address.id);
    assertThat(address.label).as("The address label should be '" + label + "'").isEqualTo(label);
  }

  @Test
  public void itShouldUpdateAddress() {
    String label = "Label";
    Address address = addressService.create(label, 48F, 1F);

    addressService.save(address);
    assertThat(address.id).as("The address should have a positive id").isGreaterThan(0);

    // Cache id to check later if the same address has been updated
    int id = address.id;

    String newLabel = "New";
    address.label = newLabel;

    // Performed update
    addressService.save(address);
    assertThat(id == address.id).as("The address id should be the same - " + id).isTrue();

    address = addressService.fetch(address.id);
    assertThat(address.label).as("The new address label should be '" + newLabel + "'").isEqualTo(newLabel);
  }

  @Test(expectedExceptions = AddressNotFoundException.class)
  public void itShouldDeleteAddress() {
    String label = "Label";
    Address address = addressService.create(label, 48F, 1F);

    addressService.save(address);
    assertThat(address.id).as("The address should have a positive id").isGreaterThan(0);

    addressService.remove(address.id);
    address = addressService.fetch(address.id);
  }
}
