package org.vas.domain.repository.impl;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;
import org.vas.domain.repository.UserService;
import org.vas.domain.repository.exception.DomainRepositoryException;

public class UserServiceImpl implements UserService {

  @Inject
  UserRepository userRepository;

  @Override
  public User fetch(int id) {
    try {
      return userRepository.queryForId(id);
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }

  @Override
  public List<User> list() {
    try {
      return userRepository.queryForAll();
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }
}
