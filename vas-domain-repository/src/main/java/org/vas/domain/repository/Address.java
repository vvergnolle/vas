package org.vas.domain.repository;

import org.vas.domain.repository.impl.AddressRepositoryImpl;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="addresses", daoClass=AddressRepositoryImpl.class)
public class Address {

	@DatabaseField(generatedId=true)
	public int id;
	
	@DatabaseField
	public String label;
	
	@DatabaseField
	public float latitude;
	
	@DatabaseField
	public float longitude;
	
	@DatabaseField(foreign = true)
	public transient User user;
}