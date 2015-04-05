package org.vas.commons.bean;

import java.security.Principal;

import org.vas.commons.security.UserPrincipal;

/**
 * Pojo that will give back informations on a user
 */
public final class UserBean {

	private int id;
	private String username;
	private long time = System.currentTimeMillis();

	public UserBean() {
		super();
	}

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
