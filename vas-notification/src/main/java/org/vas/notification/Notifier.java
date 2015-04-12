package org.vas.notification;

import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.User;
import org.vas.inject.Services;

import com.google.common.collect.Lists;

class Notifier {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected final Services services;
  protected final List<NotificationListenerDescriptor> descriptors = Lists.newArrayList();

  public Notifier(Services services) {
    super();
    this.services = services;
    init();
  }

  void init() {
    ServiceLoader.load(NotificationListenerDescriptor.class).forEach(descriptor -> descriptors.add(descriptor));
  }

  public void dispatch(User user, Address address, Notification notification) {
    descriptors.forEach(d -> doDispatch(user, address, notification, d));
  }

  protected void doDispatch(User user, Address address, Notification notification,
    NotificationListenerDescriptor descriptor) {
    if(descriptor.types().contains(notification.type)) {
      NotificationListener listener = services.get(descriptor.listener());
      listener.notify(user, address, notification, fetchStations());

      if(logger.isDebugEnabled()) {
        logger.debug("Notification {} dispatched to listener {}", notification.id, listener);
      }
    }
  }

  protected List<? super Object> fetchStations() {
    return Collections.emptyList();
  }
}
