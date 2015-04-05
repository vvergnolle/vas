package org.vas.jaxrs.test;

import javax.ws.rs.core.Application;

import org.vas.jaxrs.JaxrsDescriptor;

public class TestJaxrsApplicationDescriptor implements JaxrsDescriptor {

	@Override
	public String id() {
	  return "text-jaxrs";
	}
	
	@Override
	public Class<? extends Application> applicationClass() {
	  return TestJaxrsApplication.class;
	}
	
	@Override
	public String mapping() {
		return "/rest/test/jaxrs/*";
	}
}
