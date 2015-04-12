package org.vas.notification.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import javax.inject.Inject;

import org.testng.annotations.Test;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.AddressService;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserService;
import org.vas.notification.Notification;
import org.vas.notification.domain.repository.NotificationService;
import org.vas.test.AbstractVasRuntimeTest;

public class TestGroupedUserNotificationWorker extends AbstractVasRuntimeTest {

  private static final int NB_ADDRESS = 2;

  @Inject
  NotificationService notificationService;

  @Inject
  AddressService addressService;

  @Inject
  UserService userService;

  @Inject
  CountNotificationListener listener;

  @Test
  public void itShouldExecuteWorkers() throws Exception {
    createContext();
    Thread.sleep(1000 * 5);
    assertThat(listener.count).as("The notification counter shouldn't be equals to 0").isGreaterThan(0);
  }

  private void createContext() {
    {
      User user = userService.fetch(1);
      for (int i = 0; i < NB_ADDRESS; i++) {
        Address address = makeAddress(user);
        addressService.save(address);
        createNotification(address);
      }
    }

    {
      User user = userService.fetch(2);
      for (int i = 0; i < NB_ADDRESS; i++) {
        Address address = makeAddress(user);
        addressService.save(address);
        createNotification(address);
      }
    }

    {
      User user = userService.fetch(3);
      for (int i = 0; i < NB_ADDRESS; i++) {
        Address address = makeAddress(user);
        addressService.save(address);
        createNotification(address);
      }
    }
  }

  private void createNotification(Address address) {
    Notification notification = new Notification();
    notification.hour = new Date().getHours();
    notification.address = address;
    notification.type = "test";

    notificationService.save(notification);
  }

  private Address makeAddress(User user) {
    Address address = new Address();
    address.label = "A label (" + System.currentTimeMillis() + ")";
    address.latitude = 48.857924399999995f;
    address.longitude = 2.402112f;
    address.user = user;

    return address;
  }

}
