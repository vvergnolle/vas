package org.vas.jaxrs.stations.around;

import javax.ws.rs.core.Application;

import org.vas.jaxrs.JaxrsDescriptor;

public class StationsAroundJaxrsDescriptor implements JaxrsDescriptor {

	@Override
	public String id() {
	  return "stations-around";
	}
	
	@Override
	public String mapping() {
	  return "/rest/stations/around/*";
	}
	
	@Override
	public Class<? extends Application> applicationClass() {
	  return StationsAroundApplication.class;
	}
}
