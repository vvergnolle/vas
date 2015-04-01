package org.vas.auth.resources;

import static org.vas.jaxrs.Responses.ok;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.vas.jaxrs.VasResource;

@Path("/infos")
public class UserResource extends VasResource {

	@GET
	public Response infos() {
		return ok(currentUserBean());
	}
}