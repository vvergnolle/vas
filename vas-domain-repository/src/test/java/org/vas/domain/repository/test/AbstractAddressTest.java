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
