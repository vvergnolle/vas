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

		address = addressService.fecth(address.id);
		assertThat(address.label).as("The address label should be '"+ label +"'").isEqualTo(label);
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
		
		address = addressService.fecth(address.id);
		assertThat(address.label).as("The new address label should be '"+ newLabel +"'").isEqualTo(newLabel);
	}
	
	@Test(expectedExceptions=AddressNotFoundException.class)
	public void itShouldDeleteAddress() {
		String label = "Label";
		Address address = addressService.create(label, 48F, 1F);
		
		addressService.save(address);
		assertThat(address.id).as("The address should have a positive id").isGreaterThan(0);
		
		addressService.remove(address.id);
		address = addressService.fecth(address.id);
	}
}
