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
package org.vas.address.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import javax.inject.Inject;

import org.testng.annotations.Test;
import org.vas.commons.bean.DomainBean;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.AddressService;
import org.vas.domain.repository.exception.AddressNotFoundException;
import org.vas.test.AbstractVasRestTest;
import org.vas.test.rest.Response;

import com.google.common.collect.Maps;

public class TestJaxrsAddress extends AbstractVasRestTest {

  @Inject
  AddressService addressService;

  @Test
  public void itShouldGetAddresses() {
    Response<String> response = vasRest.withBasic("test", "test").get("/rest/address", String.class);

    assertThat(response.status).as("GET address should respond with OK").isEqualTo(200);
  }

  @Test
  public void itShouldNotDeleteAddress() {
    Response<String> response = vasRest.withBasic("test", "test").delete("/rest/address/0", String.class);

    assertThat(response.status).as("DELETE address should respond with NO CONTENT").isEqualTo(204);
  }

  @Test
  public void itShouldCreateAddress() {
    String label = "Default address";
    String lat = "48.0505";
    String lng = "1.020";

    Response<DomainBean> response = vasRest.withBasic("test", "test").post("/rest/address/",
      makeAddressMap(label, lat, lng), DomainBean.class);

    assertThat(response.status).as("POST address should respond with CREATED").isEqualTo(201);
    assertThat(response.content.getId()).as("The created address should have an id").isGreaterThan(0);

    Address address = addressService.fetch(response.content.getId());
    assertThat(address).as("The created address should be retrievable").isNotNull();
    assertThat(address.id == response.content.getId()).as(
      "The retrieved address and the created address should have the same id").isTrue();
  }

  @Test
  public void itShouldUpdateAddress() {
    String label = "Default address";
    String lat = "45.0505";
    String lng = "1.06";

    Response<DomainBean> response = vasRest.withBasic("test", "test").post("/rest/address/",
      makeAddressMap(label, lat, lng), DomainBean.class);

    int id = response.content.getId();

    assertThat(response.status).as("POST address should respond with CREATED").isEqualTo(201);
    assertThat(id).as("The created address should have an id").isGreaterThan(0);

    Address address = addressService.fetch(id);
    assertThat(address.label).as("The fetched address should have the same label than the created one")
      .isEqualTo(label);

    String newLabel = "New label";
    Map<String, String> putMap = makeAddressMap(newLabel, null, null);
    putMap.put("id", String.valueOf(id));

    Response<String> putResponse = vasRest.withBasic("test", "test").put("/rest/address/", putMap, String.class);

    assertThat(putResponse.status).as("UPDATE address should respond with OK").isEqualTo(200);

    address = addressService.fetch(id);
    assertThat(address.label).as("The retrieved address should have the new label").isEqualTo(newLabel);
  }

  @Test(expectedExceptions = AddressNotFoundException.class)
  public void itShouldRemoveAddress() {
    String label = "Default address";
    String lat = "45.0505";
    String lng = "1.06";

    Response<DomainBean> response = vasRest.withBasic("test", "test").post("/rest/address/",
      makeAddressMap(label, lat, lng), DomainBean.class);

    int id = response.content.getId();

    assertThat(response.status).as("POST address should respond with CREATED").isEqualTo(201);
    assertThat(id).as("The created address should have an id").isGreaterThan(0);

    Response<String> deleteResponse = vasRest.withBasic("test", "test").delete("/rest/address/{0}", String.class, id);

    assertThat(deleteResponse.status).as("DELETE address should respond with OK").isEqualTo(200);
    addressService.fetch(id);
  }

  private Map<String, String> makeAddressMap(String label, String latitude, String longitude) {
    Map<String, String> address = Maps.newHashMap();
    address.put("label", label);

    if(latitude != null) {
      address.put("latitude", latitude);
    }

    if(longitude != null) {
      address.put("longitude", longitude);
    }

    return address;
  }
}
