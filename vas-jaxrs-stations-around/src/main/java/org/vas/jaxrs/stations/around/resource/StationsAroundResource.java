package org.vas.jaxrs.stations.around.resource;

import static org.vas.jaxrs.Responses.ok;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.vas.domain.repository.Address;
import org.vas.http.resource.HttpResponse;
import org.vas.jaxrs.VasResource;
import org.vas.opendata.paris.client.AutolibOpendataParisWs;
import org.vas.opendata.paris.client.VelibOpendataParisWs;

@Path("/{id}")
public class StationsAroundResource extends VasResource {

	@Inject
	protected VelibOpendataParisWs velibWs;

	@Inject
	protected	AutolibOpendataParisWs autolibWs;
	
	@GET
	@Path("{distance}")
	public Response around(
		@PathParam("id") int id,
		@PathParam("distance") int distance) {

		Address address = addressService.fecth(id);

		HttpResponse autolibResponse = autolibWs.geofilter(address.latitude, address.longitude, distance);
		return ok(new String(autolibResponse.bytes()));
	}
}