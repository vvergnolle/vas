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
package org.vas.jaxrs.notification.resource;

import static org.vas.jaxrs.Responses.created;
import static org.vas.jaxrs.Responses.noContent;
import static org.vas.jaxrs.Responses.ok;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.vas.domain.repository.Address;
import org.vas.domain.repository.AddressService;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserService;
import org.vas.jaxrs.VasResource;
import org.vas.jaxrs.notification.exception.InvalidNotificationTimeException;
import org.vas.notification.Notification;
import org.vas.notification.domain.repository.NotificationService;

import com.google.inject.Inject;

@Path("/")
public class NotificationResource extends VasResource {

  @Inject
  protected NotificationService notificationService;

  @Inject
  protected AddressService addressService;

  @Inject
  protected UserService userService;

  @GET
  @Path("{id}")
  public Response get(@PathParam("id") int addressId) {
    Address address = addressService.fetch(addressId);
    if(isAnotherUser(address.user)) {
      return noContent();
    }

    return ok(notificationService.listByAddress(address));
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response create(@FormParam("address") int addressId, @FormParam("hour") int hour,
    @FormParam("min") @DefaultValue("0") int min) {
    Address address = addressService.fetch(addressId);

    // The current user can't touch another users informations
    if(isAnotherUser(address.user)) {
      return noContent();
    }

    Notification notification = new Notification();
    notification.hour = hour;
    notification.min = min;
    notification.address = address;

    if(!notification.isValidTime()) {
      throw new InvalidNotificationTimeException("Notification (" + notification
        + ") is invalid - please set a valid hour");
    }

    notificationService.save(notification);
    return created(notification.id);
  }

  @DELETE
  @Path("{id}")
  public Response del(@PathParam("id") int id) {
    notificationService.remove(id);
    return ok();
  }

  private boolean isAnotherUser(User user) {
    return !currentUser().username.equals(userService.fetch(user.id).username);
  }
}
