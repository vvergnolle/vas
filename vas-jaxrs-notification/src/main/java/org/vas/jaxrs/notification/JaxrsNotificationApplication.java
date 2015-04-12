package org.vas.jaxrs.notification;

import java.util.Arrays;
import java.util.List;

import org.vas.jaxrs.VasApplication;
import org.vas.jaxrs.notification.resource.NotificationResource;

public class JaxrsNotificationApplication extends VasApplication {

  @Override
  protected List<Object> resources() {
    return Arrays.asList(new NotificationResource());
  }
}
