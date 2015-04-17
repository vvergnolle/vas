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
      logger.trace("Dispatch notification n-{}", notification.id);
    }

    notifier.dispatch(user, address, notification);
  }
}
