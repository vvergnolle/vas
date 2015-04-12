package org.vas.notification.mail;

import java.util.List;

import org.vas.domain.repository.Address;
import org.vas.domain.repository.User;
import org.vas.mail.Mail;
import org.vas.mail.SendMailEvent;
import org.vas.notification.Notification;
import org.vas.notification.NotificationListener;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class MailNotificationListener implements NotificationListener {

  @Inject
  protected EventBus eventBus;

  @Override
  public void notify(User user, Address address, Notification notification, List<? super Object> stations) {
    Mail mail = new Mail("vincent7894@gmail.com", "no-reply@vas.org", buildBody(user, address, stations),
      "Notification about stations");

    eventBus.post(new SendMailEvent(this, mail));
  }

  private String buildBody(User user, Address address, List<? super Object> stations) {
    return "Found " + stations.size() + " stations " + "(" + stations + ") for the address " + address.label
      + ".\nSee you " + user.username;
  }
}
