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

@Path("/")
public class StationsAroundResource extends VasResource {

  private static final String FILTER_ALL = "all";
  private static final String FILTER_VELIB = "velib";
  private static final String FILTER_AUTOLIB = "autolib";

  @Inject
  protected VelibOpendataParisWs velibWs;

  @Inject
  protected AutolibOpendataParisWs autolibWs;

  @GET
  @Path("{id}/{distance}/{filter: (all|autolib|velib)}")
  public Response around(@PathParam("id") int id, @PathParam("distance") int distance,
    @PathParam("filter") String filter) {
    Address address = addressService.fetch(id);

    boolean velib = FILTER_ALL.equalsIgnoreCase(filter) || FILTER_VELIB.equalsIgnoreCase(filter);
    boolean autolib = FILTER_ALL.equalsIgnoreCase(filter) || FILTER_AUTOLIB.equalsIgnoreCase(filter);

    HttpResponse autolibResponse = autolib ? autolibWs.geofilter(address.latitude, address.longitude, distance) : null;
    HttpResponse velibResponse = velib ? velibWs.geofilter(address.latitude, address.longitude, distance) : null;

    Appendable builder = joinToJson(autolibResponse, velibResponse);
    return ok(builder.toString());
  }

  @GET
  @Path("/geo/{lat}/{lng}/{distance}/{filter: (all|autolib|velib)}")
  public Response around(@PathParam("lat") float lat, @PathParam("lng") float lng, @PathParam("distance") int distance,
    @PathParam("filter") String filter) {
    boolean velib = FILTER_ALL.equalsIgnoreCase(filter) || FILTER_VELIB.equalsIgnoreCase(filter);
    boolean autolib = FILTER_ALL.equalsIgnoreCase(filter) || FILTER_AUTOLIB.equalsIgnoreCase(filter);

    HttpResponse autolibResponse = autolib ? autolibWs.geofilter(lat, lng, distance) : null;
    HttpResponse velibResponse = velib ? velibWs.geofilter(lat, lng, distance) : null;

    Appendable builder = joinToJson(autolibResponse, velibResponse);
    return ok(builder.toString());
  }

  private Appendable joinToJson(HttpResponse autolibResponse, HttpResponse velibResponse) {
    StringBuilder builder = new StringBuilder("{");

    if(autolibResponse != null) {
      builder.append("\"autolibs\": ");
      builder.append(new String(autolibResponse.bytes()));
    }

    if(velibResponse != null) {
      if(autolibResponse != null) {
        builder.append(",");
      }

      builder.append("\"velibs\": ");
      builder.append(new String(velibResponse.bytes()));
    }

    return builder.append("}");
  }
}
