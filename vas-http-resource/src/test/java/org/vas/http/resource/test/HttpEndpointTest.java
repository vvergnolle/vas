package org.vas.http.resource.test;

import org.testng.annotations.Test;
import org.vas.http.resource.HttpEndpointFactory;
import org.vas.http.resource.HttpResponse;
import org.vas.http.resource.annotation.HttpEndpoint;
import org.vas.http.resource.annotation.HttpResource;

public class HttpEndpointTest {
	
	@HttpEndpoint("http://opendata.paris.fr/explore/dataset/stations-velib-disponibilites-en-temps-reel")
	interface OpenDataParisWS {
		
		@HttpResource(uri="?tab=api&q=&geofilter.distance=48.857924399999995,2.402112,1000")
		HttpResponse raw();
	}

	@Test
	public void itShouldMakeGetRequest() {
		OpenDataParisWS ws = HttpEndpointFactory.createEndpoint(OpenDataParisWS.class);
		ws.raw();
	}
}
