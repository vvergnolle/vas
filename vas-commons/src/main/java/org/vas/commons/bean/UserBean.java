package org.vas.commons.bean;

import java.security.Principal;

import org.vas.commons.security.UserPrincipal;

/**
 * Pojo that will give back informations on a user
 */
public final class UserBean {

	public final int id;
	public final String username;
	public final long time = System.currentTimeMillis();

	UserBean(int id, String username) {
	  super();
	  this.id = id;
	  this.username = username;
  }
	
	UserBean(int id, Principal principal) {
	  this(id, principal.getName());
  }
	
	UserBean(UserPrincipal principal) {
	  this(principal.id, principal.user);
  }
	
	public static UserBean of(UserPrincipal principal) {
		return new UserBean(principal);
	}
	
	public static UserBean of(int id, Principal principal) {
		return new UserBean(id, principal);
	}
	
	public static UserBean of(int id, String username) {
		return new UserBean(id, username);
	}
}