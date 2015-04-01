package org.vas.domain.repository;

public interface AddressService {

	Address fecth(int id);
	
	Address create(String label, float latitude, float longitude);

	void save(Address address);
}