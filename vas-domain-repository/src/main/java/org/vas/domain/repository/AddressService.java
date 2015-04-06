package org.vas.domain.repository;

import org.vas.domain.repository.exception.AddressNotFoundException;

public interface AddressService {

  /**
   * Remove an address
   */
  void remove(int id);

  /**
   * Fetch an address
   * 
   * @throws AddressNotFoundException
   */
  Address fecth(int id);

  /**
   * Create a transient address
   */
  Address create(String label, float latitude, float longitude);

  /**
   * Make the address persistent
   */
  void save(Address address);
}
