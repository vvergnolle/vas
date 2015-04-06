package org.vas.jaxrs.stations.around.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.testng.annotations.Test;
import org.vas.commons.bean.DomainBean;
import org.vas.test.AbstractVasRestTest;
import org.vas.test.rest.Response;

import com.google.common.collect.Maps;

public class TestStationsAround extends AbstractVasRestTest {

  @Test
  public void itShouldFetchStations() {
    String label = "Default address";
    String lat = "45.100577";
    String lng = "1.10102";

    Response<DomainBean> response = vasRest.withBasic("test", "test").post("/rest/address/",
      makeAddressMap(label, lat, lng), DomainBean.class);

    int id = response.content.getId();

    assertThat(response.status).as("POST address should respond with CREATED").isEqualTo(201);
    assertThat(id).as("The created address should have an id").isGreaterThan(0);

    Response<Void> stationsResponse = vasRest.withBasic("test", "test").get("/rest/stations/around/{0}/0", Void.class,
      id);

    assertThat(stationsResponse.status).as("GET stations should respond with OK").isEqualTo(200);
  }

  private Map<String, String> makeAddressMap(String label, String latitude, String longitude) {
    Map<String, String> address = Maps.newHashMap();
    address.put("label", label);
    address.put("latitude", latitude);
    address.put("longitude", longitude);

    return address;
  }
}
