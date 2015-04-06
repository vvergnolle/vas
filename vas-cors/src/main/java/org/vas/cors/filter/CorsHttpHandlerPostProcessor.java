package org.vas.cors.filter;

import io.undertow.attribute.RequestHeaderAttribute;
import io.undertow.util.Headers;

import org.vas.commons.context.BootContext;
import org.vas.commons.http.HttpHandlerPostProcessor;

public class CorsHttpHandlerPostProcessor implements HttpHandlerPostProcessor {

  private static final String ALLOW_ORIGIN = "Access-Control-Allow-Origin";

  @Override
  public void postProcess(BootContext context) {
    context.addPredicate(CorsHttpHandler.PRIORITY, CorsHttpHandler.predicate(), new CorsHttpHandler());

    // Flush the allow origin header in responses
    context.addHeader(ALLOW_ORIGIN, new RequestHeaderAttribute(Headers.ORIGIN));
  }
}
