package org.vas.domain.repository.impl;

import java.sql.SQLException;

import javax.inject.Inject;

import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;
import org.vas.domain.repository.UserService;

public class UserServiceImpl implements UserService {

	@Inject
	UserRepository userRepository;
	
	@Override
	public User fetch(int id) {
	  try {
	    return userRepository.queryForId(id);
    } catch (SQLException e) {
    	throw new RuntimeException(e);
    }
	}
}
