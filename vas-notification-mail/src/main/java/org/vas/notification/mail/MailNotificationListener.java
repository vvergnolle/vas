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
package org.vas.notification.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.station.LibStations;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.User;
import org.vas.mail.Mail;
import org.vas.mail.SendMailEvent;
import org.vas.notification.Notification;
import org.vas.notification.NotificationListener;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.resolver.DefaultResolver;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class MailNotificationListener implements NotificationListener {

  private static final String NO_REPLY = "no-reply@vas.org";
  private static final String NOTIFICATION_TPL = "mails-tpl/notification.tpl";
  private static final MustacheFactory MUSTACHE_FACTORY = new DefaultMustacheFactory(new DefaultResolver());

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected EventBus eventBus;
  protected Mustache notificationTemplate;

  @Inject
  public void init(EventBus eventBus) throws IOException {
    this.eventBus = eventBus;
    notificationTemplate = MUSTACHE_FACTORY.compile(NOTIFICATION_TPL);
  }

  @Override
  public void notify(User user, Address address, Notification notification, LibStations stations) {
    Mail mail = new Mail(user.email, NO_REPLY, buildBody(user, address, notification, stations),
      "Notification about stations");

    eventBus.post(new SendMailEvent(this, mail));
  }

  private String buildBody(User user, Address address, Notification notification, LibStations stations) {
    Map<String, Object> ctx = Maps.newHashMap();
    ctx.put("user", user);
    ctx.put("address", address);
    ctx.put("stations", stations);
    ctx.put("notification", notification);

    StringWriter writer = new StringWriter();
    notificationTemplate.execute(writer, ctx);
    return writer.toString();
  }
}
