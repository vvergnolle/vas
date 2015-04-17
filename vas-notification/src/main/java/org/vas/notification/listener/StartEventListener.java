/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
