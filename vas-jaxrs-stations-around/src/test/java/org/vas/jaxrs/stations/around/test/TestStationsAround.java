/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

    Response<Void> stationsResponse = vasRest.withBasic("test", "test").get("/rest/stations/around/{0}/0/all",
      Void.class, id);

    assertThat(stationsResponse.status).as("GET stations should respond with OK").isEqualTo(200);
  }

  @Test
  public void itShouldGeoFetchStations() {
    String lat = "45.100577";
    String lng = "1.10105";

    Response<Void> response = vasRest.withBasic("test", "test").get("/rest/stations/around/geo/{0}/{1}/0/all",
      Void.class, lat, lng);

    assertThat(response.status).as("GET stations should respond with OK").isEqualTo(200);
  }

  private Map<String, String> makeAddressMap(String label, String latitude, String longitude) {
    Map<String, String> address = Maps.newHashMap();
    address.put("label", label);
    address.put("latitude", latitude);
    address.put("longitude", longitude);

    return address;
  }
}
