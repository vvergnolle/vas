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
