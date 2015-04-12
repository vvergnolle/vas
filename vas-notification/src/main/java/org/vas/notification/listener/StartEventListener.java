package org.vas.notification.listener;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.event.StartEvent;
import org.vas.domain.repository.UserService;
import org.vas.notification.GroupedUsersNotificationWorker;
import org.vas.notification.domain.repository.NotificationService;
import org.vas.worker.WorkerService;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class StartEventListener {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  protected WorkerService workerService;

  @Inject
  protected UserService userService;

  @Inject
  protected NotificationService notificationService;

  protected int delay;
  protected int initialDelay;
  protected TimeUnit timeUnit;

  public StartEventListener(Properties properties) {
    super();
    String delay = properties.getProperty("vas.notification.worker.delay", "1");
    String initialDelay = properties.getProperty("vas.notification.worker.initialDelay", "1");
    String timeUnit = properties.getProperty("vas.notification.worker.timeUnit", TimeUnit.HOURS.toString());

    this.delay = Integer.valueOf(delay);
    this.initialDelay = Integer.valueOf(initialDelay);
    this.timeUnit = TimeUnit.valueOf(timeUnit.toUpperCase());

    if(logger.isDebugEnabled()) {
      logger.debug("Delay {}", this.delay);
      logger.debug("InitialDelay {}", this.initialDelay);
      logger.debug("TimeUnit {}", this.timeUnit);
    }
  }

  @Inject
  public void autoRegister(EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void onStart(StartEvent event) {
    if(logger.isDebugEnabled()) {
      logger.debug("Schedule notification main worker");
    }

    workerService.schedule(new GroupedUsersNotificationWorker(workerService, userService, notificationService),
      initialDelay, delay, timeUnit);
  }
}
