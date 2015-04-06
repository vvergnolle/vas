package org.vas.jaxrs.test.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.vas.jaxrs.Responses;
import org.vas.jaxrs.VasResource;

@Path("/foobar")
public class FooBarResource extends VasResource {

  public static final String STRING = "foobar";

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response invoke() {
    return Responses.ok(STRING);
  }
}
