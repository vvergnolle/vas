package org.vas.exceptions.test;

import javax.ws.rs.core.Application;

import org.vas.jaxrs.JaxrsDescriptor;

public class TestJaxrsDescriptor implements JaxrsDescriptor {

	@Override
	public String id() {
	  return "test-exceptions";
	}
	
	@Override
	public Class<? extends Application> applicationClass() {
	  return TestExceptionsApplication.class;
	}

	@Override
	public String mapping() {
	  return "/rest/test/exceptions/*";
	}
}