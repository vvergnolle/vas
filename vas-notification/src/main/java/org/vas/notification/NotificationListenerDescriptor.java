package org.vas.notification;

import java.util.List;

public interface NotificationListenerDescriptor {

  /**
   * Notification types
   */
  List<String> types();

  Class<? extends NotificationListener> listener();
}
