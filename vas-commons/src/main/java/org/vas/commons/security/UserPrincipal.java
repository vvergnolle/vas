package org.vas.commons.security;

import io.undertow.security.idm.Account;

import java.security.Principal;
import java.util.Set;

/**
 * Java {@link Principal} that will be used to hold our logged user informations.
 * 
 * Instances of this class will be used in servlets or handlers.
 */
public class UserPrincipal implements Account, Principal {
	
	public final int id;
	public final String user;
	protected Set<String> roles;
	
	public UserPrincipal(int id, String user, Set<String> roles) {
	  super();
	  this.id = id;
	  this.user = user;
	  this.roles = roles;
  }

	@Override
  public Principal getPrincipal() {
	  return this;
  }

	@Override
  public Set<String> getRoles() {
	  return roles;
  }

	@Override
  public String getName() {
	  return user;
  }
}