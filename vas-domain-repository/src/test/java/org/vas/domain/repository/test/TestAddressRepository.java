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

import java.sql.SQLException;

import org.testng.annotations.Test;
import org.vas.domain.repository.Address;

public class TestAddressRepository extends AbstractAddressTest {

  @Test
  public void itShouldCreateAndReadAddress() {
    String label = "Fake label";
    Address address = makeAddress(label);

    saveAddress(address);
    address = fetchAddress(address.id);
    assertThat(address.label).as("The address label should be equals to '" + label + "'").isEqualTo(label);
  }

  @Test
  public void itShouldUpdateAddress() {
    String label = "Initial";

    Address address = makeAddress(label);
    saveAddress(address);

    assertThat(address.id).as("The saved address should have a positive id").isGreaterThan(0);

    String newLabel = "New";
    address.label = newLabel;
    saveAddress(address);

    assertThat(address.label).as("The address should have a new label aka '" + newLabel + "'").isEqualTo(newLabel);
  }

  @Test
  public void itShouldDeleteAddress() {
    Address address = makeAddress("Label");
    saveAddress(address);

    assertThat(address.id).as("The saved address should have an id").isGreaterThan(0);
    deleteAddress(address);

    address = fetchAddress(address.id);
    assertThat(address).as("The address should be null").isNull();
  }

  protected void deleteAddress(Address address) {
    try {
      addressRepository.delete(address);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  protected void saveAddress(Address address) {
    try {
      if(address.id <= 0) {
        addressRepository.create(address);
      } else {
        addressRepository.update(address);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  protected Address fetchAddress(int id) {
    try {
      return addressRepository.queryForId(id);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  protected Address makeAddress(String label) {
    Address address = new Address();
    address.label = label;
    address.latitude = 48.F;
    address.longitude = 1.F;
    address.user = testUser();

    return address;
  }
}
