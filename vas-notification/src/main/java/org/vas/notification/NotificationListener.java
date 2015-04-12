package org.vas.notification;

import java.util.List;

import org.vas.domain.repository.Address;
import org.vas.domain.repository.User;

public interface NotificationListener {

  void notify(User user, Address address, Notification notification, List<? super Object> stations);
}
