package org.vas.notification;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserService;
import org.vas.inject.Services;
import org.vas.inject.ServicesUtil;
import org.vas.notification.domain.repository.NotificationService;
import org.vas.worker.WorkerService;

public class GroupedUsersNotificationWorker implements Runnable {

  static final double USERS_PER_WORKER = 1.0;

  protected final Services services;
  protected final Notifier notifier;
  protected final UserService userService;
  protected final WorkerService workerService;
  protected final NotificationService notificationService;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  public GroupedUsersNotificationWorker(WorkerService workerService, UserService userService,
    NotificationService notificationService) {
    super();
    this.userService = userService;
    this.workerService = workerService;
    this.notificationService = notificationService;
    this.services = ServicesUtil.defaultContainer();
    this.notifier = new Notifier(services);
  }

  @Override
  public void run() {
    if(logger.isDebugEnabled()) {
      logger.debug("Start grouped users notification worker");
    }

    List<User> users = userService.list();
    int times = (int) Math.ceil(users.size() / USERS_PER_WORKER);

    for (int i = 0; i < times; i++) {
      int start = i * (int) USERS_PER_WORKER;

      Runnable runnable = null;
      try {
        runnable = new NotificationWorker(users.subList(start, start + (int) USERS_PER_WORKER), notificationService,
          notifier);
      } catch (IndexOutOfBoundsException e) {
        runnable = new NotificationWorker(users.subList(start, users.size()), notificationService, notifier);
      } finally {
        workerService.start(runnable);
      }
    }
  }
}
