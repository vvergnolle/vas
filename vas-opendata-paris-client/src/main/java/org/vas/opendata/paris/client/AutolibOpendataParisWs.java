package org.vas.opendata.paris.client;

import org.vas.http.resource.HttpResponse;
import org.vas.http.resource.annotation.HttpEndpoint;
import org.vas.http.resource.annotation.HttpResource;

@HttpEndpoint("http://opendata.paris.fr/api/records/1.0/search?dataset=stations_et_espaces_autolib_de_la_metropole_parisienne&")
public interface AutolibOpendataParisWs {

	@HttpResource(uri="geofilter.distance=%f,%f,%d")
	HttpResponse geofilter(float lat, float lng, int distance);

	@HttpResource(uri="start=%d&rows=%d&geofilter.distance=%f,%f,%d")
	HttpResponse geofilter(int start, int limit, float lat, float lng, int distance);
}