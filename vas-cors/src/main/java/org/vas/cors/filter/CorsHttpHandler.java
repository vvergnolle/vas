package org.vas.cors.filter;

import io.undertow.attribute.ExchangeAttributes;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorsHttpHandler implements HttpHandler {
	
	static int PRIORITY = 100;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static Predicate predicate() {
		return Predicates.contains(ExchangeAttributes.requestMethod(), Methods.OPTIONS_STRING);
	}

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		if(exchange.isInIoThread()) {
			exchange.dispatch(this);
			return;
		}
		
		if(logger.isTraceEnabled()) {
			logger.trace("Attach CORS headers");
		}
		
		HeaderMap httpHeaders = exchange.getResponseHeaders();
		HeaderMap requestHeaders = exchange.getRequestHeaders();

		String methods = requestHeaders.getFirst("Access-Control-Request-Method");
		if (methods != null) {
			httpHeaders.addFirst(new HttpString("Access-Control-Allow-Methods"), methods);
		}

		String headers = requestHeaders.getFirst("Access-Control-Request-Headers");
		if (headers != null) {
			httpHeaders.addFirst(new HttpString("Access-Control-Allow-Headers"), headers);
		}
		
		httpHeaders.addFirst(new HttpString("Access-Control-Allow-Credentials"), "true");
	}
}
