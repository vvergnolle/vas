package org.vas.domain.repository.impl;

import org.vas.domain.repository.AddressRepository;
import org.vas.domain.repository.Repositories;
import org.vas.domain.repository.UserRepository;

import com.google.inject.Inject;

public final class RepositoriesImpl implements Repositories {

  @Inject
  UserRepository userRepository;

  @Inject
  AddressRepository addressRepository;

  @Override
  public UserRepository user() {
    return userRepository;
  }

  @Override
  public AddressRepository address() {
    return addressRepository;
  }
}
