package org.vas.auth;

import javax.ws.rs.core.Application;

import org.vas.jaxrs.JaxrsDescriptor;

public class UserApplicationDescriptor implements JaxrsDescriptor {

	@Override
	public String id() {
		return "users-infos";
	}
	
	@Override
	public String mapping() {
	  return "/rest/users/*";
	}
	
	@Override
	public Class<? extends Application> applicationClass() {
	  return UserApplication.class;
	}
}