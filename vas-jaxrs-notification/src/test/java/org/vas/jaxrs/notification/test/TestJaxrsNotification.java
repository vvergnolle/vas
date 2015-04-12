package org.vas.jaxrs.notification.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Map;

import org.testng.annotations.Test;
import org.vas.commons.bean.DomainBean;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.AddressService;
import org.vas.domain.repository.UserService;
import org.vas.notification.domain.repository.NotificationService;
import org.vas.notification.exception.NotificationNotFoundException;
import org.vas.test.AbstractVasRestTest;
import org.vas.test.rest.Response;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class TestJaxrsNotification extends AbstractVasRestTest {

  @Inject
  protected NotificationService notificationService;

  @Inject
  protected UserService userService;

  @Inject
  protected AddressService addressService;

  @Test
  public void itShouldFetchNotifications() {
    Address address = createAddress();
    Response<DomainBean> postResponse = vasRest.withBasic("test", "test").post("/rest/notifications",
      makeNotification(address), DomainBean.class);

    int id = postResponse.content.getId();
    assertThat(postResponse.status).as("POST notification response should be equals to 201").isEqualTo(201);
    assertThat(id).as("The created notification id should be valid").isGreaterThan(0);

    Response<String> getResponse = vasRest.withBasic("test", "test").get("/rest/notifications/{0}", String.class, id);
    assertThat(getResponse.status).as("GET notifications response status should be equals to 200").isEqualTo(200);
    assertThat(getResponse.content).as("GET notifications response content shouldn't be empty").isNotEmpty();
  }

  @Test(expectedExceptions = NotificationNotFoundException.class)
  public void itShouldCreateAndRemoveNotification() {
    Address address = createAddress();
    Response<DomainBean> postResponse = vasRest.withBasic("test", "test").post("/rest/notifications",
      makeNotification(address), DomainBean.class);

    int id = postResponse.content.getId();
    assertThat(postResponse.status).as("POST notification response should be equals to 201").isEqualTo(201);
    assertThat(id).as("The created notification id should be valid").isGreaterThan(0);

    Response<Void> deleteResponse = vasRest.withBasic("test", "test").delete("/rest/notifications/{0}", Void.class, id);
    assertThat(deleteResponse.status).as("DELETE notification response status should be equals to 200").isEqualTo(200);

    notificationService.fetch(id);
  }

  protected Address createAddress() {
    Address address = new Address();
    address.label = "Foo";
    address.latitude = 48.F;
    address.longitude = 1.F;
    address.user = userService.fetch(1);

    addressService.save(address);
    return address;
  }

  protected Map<String, String> makeNotification(Address address) {
    Map<String, String> map = Maps.newHashMap();
    map.put("address", String.valueOf(address.id));
    map.put("hour", String.valueOf(LocalDateTime.now().get(ChronoField.HOUR_OF_DAY)));

    return map;
  }

}
