package org.vas.notification.mail;

import java.util.Arrays;
import java.util.List;

import org.vas.notification.NotificationListener;
import org.vas.notification.NotificationListenerDescriptor;

public class MailNotificationListenerDescriptor implements NotificationListenerDescriptor {

  @Override
  public Class<? extends NotificationListener> listener() {
    return MailNotificationListener.class;
  }

  @Override
  public List<String> types() {
    return Arrays.asList("mail", "email");
  }
}
