package org.vas.notification.domain.repository;

import java.util.List;

import org.vas.domain.repository.Address;
import org.vas.notification.Notification;
import org.vas.notification.exception.NotificationNotFoundException;

public interface NotificationService {

  /**
   * Fetch a notification
   * 
   * @throws NotificationNotFoundException
   */
  Notification fetch(int id);

  /**
   * Remove a notification
   * 
   * @throws NotificationNotFoundException
   */
  void remove(int id);

  void save(Notification notification);

  List<Notification> listByAddress(Address address);
}
