package org.vas.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.User;
import org.vas.notification.domain.repository.NotificationService;

/**
 * Notification worker for limited users
 *
 */
public class NotificationWorker implements Runnable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected final List<User> users;
  protected final Notifier notifier;
  protected final NotificationService notificationService;

  public NotificationWorker(List<User> users, NotificationService notificationService, Notifier notifier) {
    super();
    this.users = users;
    this.notifier = notifier;
    this.notificationService = notificationService;
  }

  @Override
  public void run() {
    if(logger.isTraceEnabled()) {
      logger.trace("Start worker with {} users", users);
    }

    users.forEach(this::notifyUser);
  }

  protected void notifyUser(User user) {
    user.addresses.forEach((address) -> notifyAddress(user, address));
  }

  protected void notifyAddress(User user, Address address) {
    if(logger.isTraceEnabled()) {
      logger.trace("Notify address {} - {}", user.username, address.label);
    }

    notificationService.listByAddress(address).forEach(notif -> doNotify(user, address, notif));
  }

  protected void doNotify(User user, Address address, Notification notification) {
    if(notification.isTime(LocalDateTime.now())) {
      dispatch(user, address, notification);
    }
  }

  protected void dispatch(User user, Address address, Notification notification) {
    if(logger.isTraceEnabled()) {
      logger.trace("Dispatch notification {}", notification.id);
    }

    notifier.dispatch(user, address, notification);
  }
}
