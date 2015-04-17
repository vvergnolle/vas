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
package org.vas.auth;

import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.security.UserPrincipal;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;
import org.vas.domain.repository.exception.UserNotFoundException;

import com.google.inject.Inject;

public class RepositoryIdentityManager implements IdentityManager {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  protected UserRepository repository;

  @Override
  public Account verify(Account account) {
    return account;
  }

  @Override
  public Account verify(String id, Credential credential) {
    PasswordCredential passwordCredential = (PasswordCredential) credential;

    try {
      User user = repository.authenticate(id, passwordCredential.getPassword());
      if(logger.isDebugEnabled()) {
        logger.debug("User {} logged", user);
      }

      return new UserPrincipal(user.id, user.username, User.ROLES);
    } catch (UserNotFoundException e) {
      if(logger.isInfoEnabled()) {
        logger.info("Login failed", e);
      }
    }

    return null;
  }

  @Override
  public Account verify(Credential credential) {
    if(logger.isDebugEnabled()) {
      logger.debug("verify credential called - nothing to do");
    }

    return null;
  }
}
