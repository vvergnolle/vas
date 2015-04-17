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
package org.vas.jaxrs.address.resource;

import static org.vas.jaxrs.Responses.created;
import static org.vas.jaxrs.Responses.ok;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.vas.domain.repository.Address;
import org.vas.jaxrs.VasResource;
import org.vas.opendata.paris.client.AutolibOpendataParisWs;
import org.vas.opendata.paris.client.VelibOpendataParisWs;

@Path("/")
public class AddressResource extends VasResource {

  @Inject
  protected AutolibOpendataParisWs autolibWs;

  @Inject
  protected VelibOpendataParisWs velibWs;

  @GET
  public Response get() {
    return ok(currentUser().addresses);
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response create(@FormParam("label") String label, @FormParam("latitude") float lat,
    @FormParam("longitude") float lng) {

    Address address = addressService.create(label, lat, lng);
    address.user = currentUser();

    addressService.save(address);
    return created(address.id);
  }

  @PUT
  public Response update(@FormParam("id") int id, @FormParam("label") @DefaultValue("") String label,
    @FormParam("latitude") @DefaultValue("0") float lat, @FormParam("longitude") @DefaultValue("0") float lng) {

    Address address = addressService.fetch(id);
    boolean changes = false;

    if(!label.isEmpty()) {
      address.label = label;
      changes = true;
    }

    if(lat > 0) {
      address.latitude = lat;
      changes = true;
    }

    if(lng > 0) {
      address.longitude = lng;
      changes = true;
    }

    if(changes) {
      addressService.save(address);
    }

    return ok();
  }

  @DELETE
  @Path("{id}")
  public Response delete(@PathParam("id") int id) {
    addressService.remove(id);
    return ok();
  }
}
