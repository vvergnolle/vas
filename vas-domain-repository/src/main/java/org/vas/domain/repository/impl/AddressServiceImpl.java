package org.vas.domain.repository.impl;

import java.sql.SQLException;

import javax.inject.Inject;

import org.vas.domain.repository.Address;
import org.vas.domain.repository.AddressRepository;
import org.vas.domain.repository.AddressService;

public class AddressServiceImpl implements AddressService {
	
	@Inject
	AddressRepository repository;
	
	@Override
	public Address fecth(int id) {
	  try {
	    return repository.queryForId(id);
    } catch (SQLException e) {
    	throw new RuntimeException(e);
    }
	}
	
	@Override
	public Address create(String label, float latitude, float longitude) {
	  Address address = new Address();
	  address.label = label;
	  address.latitude = latitude;
	  address.longitude = longitude;
	  
	  return address;
	}

	@Override
	public void save(Address address) {
	  try {
	    repository.create(address);
    } catch (SQLException e) {
    	throw new RuntimeException(e);
    }
	}
}