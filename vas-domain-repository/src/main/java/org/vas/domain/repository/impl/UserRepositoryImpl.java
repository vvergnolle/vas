package org.vas.domain.repository.impl;

import java.sql.SQLException;

import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;
import org.vas.domain.repository.exception.DomainRepositoryException;
import org.vas.domain.repository.exception.UserNotFoundException;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class UserRepositoryImpl extends BaseDaoImpl<User, Integer> implements UserRepository {

  public UserRepositoryImpl(Class<User> dataClass) throws SQLException {
    super(dataClass);
  }

  public UserRepositoryImpl(ConnectionSource connectionSource, Class<User> dataClass) throws SQLException {
    super(connectionSource, dataClass);
  }

  public UserRepositoryImpl(ConnectionSource connectionSource, DatabaseTableConfig<User> tableConfig)
    throws SQLException {
    super(connectionSource, tableConfig);
  }

  @Override
  public User authenticate(String username, char[] password) {
    try {
      User user = queryBuilder().limit(1L).selectColumns(User.ID, User.USERNAME).where().eq(User.USERNAME, username)
        .and().eq(User.PASSWORD, User.hashPassword(password)).queryForFirst();

      if(user == null) {
        throw new UserNotFoundException(username);
      }

      return user;
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }
}
