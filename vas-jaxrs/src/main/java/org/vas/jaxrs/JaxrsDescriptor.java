package org.vas.jaxrs;

import javax.ws.rs.core.Application;

public interface JaxrsDescriptor {

  String id();

  String mapping();

  Class<? extends Application> applicationClass();
}
