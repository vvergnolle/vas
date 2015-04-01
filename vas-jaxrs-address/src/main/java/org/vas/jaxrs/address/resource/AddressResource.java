package org.vas.jaxrs.address.resource;

import static org.vas.jaxrs.Responses.createdSuccess;
import static org.vas.jaxrs.Responses.ok;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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
	public Response create(
		@FormParam("label") String label,
		@FormParam("latitude") float lat,
		@FormParam("longitude") float lng) {
		
		Address address = addressService.create(label, lat, lng);
		address.user = currentUser();
		
		addressService.save(address);
		return createdSuccess("/stations/around/" + address.id + "/", "Address created");
	}
	
	@PUT
	public Response update(
			@FormParam("label") String label,
			@FormParam("latitude") float lat,
			@FormParam("longitude") float lng) {

		return ok();
	}
	
	@DELETE
	public Response delete() {
		return ok();
	}
}