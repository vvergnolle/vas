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
