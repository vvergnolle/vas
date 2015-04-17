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
