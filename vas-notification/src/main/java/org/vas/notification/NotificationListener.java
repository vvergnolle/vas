package org.vas.notification;

import org.vas.commons.station.LibStations;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.User;

public interface NotificationListener {

  void notify(User user, Address address, Notification notification, LibStations stations);
}
