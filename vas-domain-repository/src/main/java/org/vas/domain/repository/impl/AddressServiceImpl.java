package org.vas.domain.repository.impl;

import java.sql.SQLException;

import javax.inject.Inject;

import org.vas.domain.repository.Address;
import org.vas.domain.repository.AddressRepository;
import org.vas.domain.repository.AddressService;
import org.vas.domain.repository.exception.AddressNotFoundException;
import org.vas.domain.repository.exception.DomainRepositoryException;

public class AddressServiceImpl implements AddressService {

  @Inject
  protected AddressRepository repository;

  @Override
  public void remove(int id) {
    try {
      if(repository.deleteById(id) < 1) {
        throw new AddressNotFoundException("Address " + id + " not found");
      }
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }

  @Override
  public Address fetch(int id) {
    try {
      Address address = repository.queryForId(id);
      if(address == null) {
        throw new AddressNotFoundException();
      }

      return address;
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
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
      if(address.id == 0) {
        repository.create(address);
      } else {
        repository.update(address);
      }
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }
}
