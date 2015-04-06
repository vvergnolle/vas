package org.vas.domain.repository.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import javax.inject.Inject;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.vas.domain.repository.AddressRepository;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;
import org.vas.test.AbstractVasRuntimeTest;

public abstract class AbstractAddressTest extends AbstractVasRuntimeTest {

  @Inject
  AddressRepository addressRepository;

  @Inject
  UserRepository userRepository;

  @Test
  public void itShouldHaveAddressRepository() {
    assertThat(addressRepository).as("The address repository shouldn't be null").isNotNull();
  }

  @Test
  public void itShouldHaveUserRepository() {
    assertThat(userRepository).as("The user repository shouldn't be null").isNotNull();
  }

  @AfterMethod
  public void clearContext() {
    try {
      int removed = addressRepository.deleteBuilder().delete();
      if(logger.isDebugEnabled()) {
        logger.debug("Clear context - remove {} addresses to keep independent the method context call", removed);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  protected User testUser() {
    try {
      return userRepository.queryForId(1);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
