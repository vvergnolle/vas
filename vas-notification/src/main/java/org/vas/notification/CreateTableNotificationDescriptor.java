package org.vas.notification;

import org.vas.domain.repository.CreateTableDescriptor;

public class CreateTableNotificationDescriptor implements CreateTableDescriptor {

  @Override
  public Class<?> domain() {
    return Notification.class;
  }

  public int order() {
    return 100;
  }
}
