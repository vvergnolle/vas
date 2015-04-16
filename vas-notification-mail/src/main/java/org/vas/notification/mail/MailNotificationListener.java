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

  static final String NOTIFICATION_TPL = "mails-tpl/notification.tpl";
  static final MustacheFactory MUSTACHE_FACTORY = new DefaultMustacheFactory(new DefaultResolver());

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
    Mail mail = new Mail("vincent7894@gmail.com", "no-reply@vas.org", buildBody(user, address, notification, stations),
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
