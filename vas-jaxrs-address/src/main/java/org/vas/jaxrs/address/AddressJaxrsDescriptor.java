package org.vas.jaxrs.address;

import javax.ws.rs.core.Application;

import org.vas.jaxrs.JaxrsDescriptor;

public class AddressJaxrsDescriptor implements JaxrsDescriptor {

	@Override
	public String id() {
	  return new String("address");
	}
	
	@Override
  public String mapping() {
	  return new String("/rest/address/*");
  }
	
	@Override
  public Class<? extends Application> applicationClass() {
	  return AddressApplication.class;
  }
}