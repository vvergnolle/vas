package org.vas.exceptions.test.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.vas.jaxrs.VasResource;

@Path("/nop")
public class NopResource extends VasResource {

	@GET
	public Response invoke() {
		throw new UnsupportedOperationException();
	}
}