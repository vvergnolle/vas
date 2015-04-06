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
