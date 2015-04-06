package org.vas.domain.repository;

import org.vas.domain.repository.exception.UserNotFoundException;

import com.j256.ormlite.dao.Dao;

public interface UserRepository extends Dao<User, Integer> {

  /**
   * Try to authenticate a user with the passed username and password
   * 
   * @throws UserNotFoundException
   */
  User authenticate(String username, char[] password);
}
