package org.vas.jaxrs.notification;

import javax.ws.rs.core.Application;

import org.vas.jaxrs.JaxrsDescriptor;

public class JaxrsNotificationDescriptor implements JaxrsDescriptor {

  @Override
  public String id() {
    return "jaxrs-notification";
  }

  @Override
  public String mapping() {
    return "/rest/notifications/*";
  }

  @Override
  public Class<? extends Application> applicationClass() {
    return JaxrsNotificationApplication.class;
  }
}
