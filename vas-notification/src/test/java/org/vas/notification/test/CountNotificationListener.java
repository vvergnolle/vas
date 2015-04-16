package org.vas.notification.test;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.station.LibStations;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.User;
import org.vas.notification.Notification;
import org.vas.notification.NotificationListener;
import org.vas.notification.NotificationListenerDescriptor;

public class CountNotificationListener implements NotificationListenerDescriptor, NotificationListener {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  int count;

  @Override
  public Class<? extends NotificationListener> listener() {
    return getClass();
  }

  @Override
  public List<String> types() {
    return Arrays.asList("test");
  }

  @Override
  public void notify(User user, Address address, Notification notification, LibStations stations) {
    if(logger.isDebugEnabled()) {
      logger.debug("Notify notification {} for the user {}", notification.id, user.username);
    }

    count++;
  }
}
