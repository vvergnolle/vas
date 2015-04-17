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
