package org.vas.opendata.paris.client;

import org.vas.http.resource.HttpResponse;
import org.vas.http.resource.annotation.HttpEndpoint;
import org.vas.http.resource.annotation.HttpResource;

@HttpEndpoint("http://opendata.paris.fr/api/records/1.0/search?dataset=stations-velib-disponibilites-en-temps-reel&")
public interface VelibOpendataParisWs {
	
	@HttpResource(uri="geofilter.distance=%f,%f,%d")
	HttpResponse geofilter(float lat, float lng, int distance);

	@HttpResource(uri="&start=%d&rows=%d&geofilter.distance=%f,%f,%d")
	HttpResponse geofilter(int start, int limit, float lat, float lng, int distance);
}