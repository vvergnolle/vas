package org.vas.domain.repository.impl;

import java.sql.SQLException;

import org.vas.domain.repository.Address;
import org.vas.domain.repository.AddressRepository;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class AddressRepositoryImpl extends BaseDaoImpl<Address, Integer> implements AddressRepository {

  public AddressRepositoryImpl(Class<Address> dataClass) throws SQLException {
    super(dataClass);
  }

  public AddressRepositoryImpl(ConnectionSource connectionSource, Class<Address> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public AddressRepositoryImpl(ConnectionSource connectionSource, DatabaseTableConfig<Address> tableConfig)
    throws SQLException {
    super(connectionSource, tableConfig);
  }
}
