package org.vas.jaxrs;

import static org.vas.jaxrs.Responses.ok;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.bean.UserBean;
import org.vas.commons.security.UserPrincipal;
import org.vas.domain.repository.AddressService;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserService;

@Produces(MediaType.APPLICATION_JSON)
public abstract class VasResource {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  protected UserService userService;

  @Inject
  protected AddressService addressService;

  @Context
  protected SecurityContext securityContext;

  @GET
  @Path("ready")
  public Response ready() {
    return ok();
  }

  protected User currentUser() {
    Principal principal = securityContext.getUserPrincipal();
    UserPrincipal user = (UserPrincipal) principal;

    return userService.fetch(user.id);
  }

  protected UserBean currentUserBean() {
    Principal principal = securityContext.getUserPrincipal();
    UserPrincipal user = (UserPrincipal) principal;

    return UserBean.of(user);
  }
}
