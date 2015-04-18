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
package org.vas.jaxrs.stations.around.resource;

import static org.vas.jaxrs.Responses.ok;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.vas.commons.conf.PaginationConf;
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

  @Inject
  protected PaginationConf paginationConf;

  @GET
  @Path("{id}/{distance}/{filter: (all|autolib|velib)}")
  public Response around(@PathParam("id") int id, @PathParam("distance") int distance,
    @PathParam("filter") String filter, @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("rows") @DefaultValue("20") int rows) {
    Address address = addressService.fetch(id);
    if(rows <= 0 || rows > paginationConf.maxRows) {
      rows = paginationConf.rows;
    }

    int start = page * rows;
    boolean velib = FILTER_ALL.equalsIgnoreCase(filter) || FILTER_VELIB.equalsIgnoreCase(filter);
    boolean autolib = FILTER_ALL.equalsIgnoreCase(filter) || FILTER_AUTOLIB.equalsIgnoreCase(filter);

    HttpResponse autolibResponse = autolib ? autolibWs.geofilter(start, rows, address.latitude, address.longitude,
      distance) : null;
    HttpResponse velibResponse = velib ? velibWs.geofilter(start, rows, address.latitude, address.longitude, distance)
      : null;

    Appendable builder = joinToJson(autolibResponse, velibResponse);
    return ok(builder.toString());
  }

  @GET
  @Path("/geo/{lat}/{lng}/{distance}/{filter: (all|autolib|velib)}")
  public Response around(@PathParam("lat") float lat, @PathParam("lng") float lng, @PathParam("distance") int distance,
    @PathParam("filter") String filter, @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("rows") @DefaultValue("20") int rows) {
    if(rows <= 0 || rows > paginationConf.maxRows) {
      rows = paginationConf.rows;
    }

    int start = page * rows;
    boolean velib = FILTER_ALL.equalsIgnoreCase(filter) || FILTER_VELIB.equalsIgnoreCase(filter);
    boolean autolib = FILTER_ALL.equalsIgnoreCase(filter) || FILTER_AUTOLIB.equalsIgnoreCase(filter);

    HttpResponse autolibResponse = autolib ? autolibWs.geofilter(start, rows, lat, lng, distance) : null;
    HttpResponse velibResponse = velib ? velibWs.geofilter(start, rows, lat, lng, distance) : null;

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
