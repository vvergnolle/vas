package org.vas.opendata.paris.proxy;

import io.undertow.server.HttpHandler;

import org.vas.commons.http.HttpHandlerDescriptor;

public class ProxyCacheHttpHandlerDescriptor implements HttpHandlerDescriptor {

  static final String URI = "/ws/opendata";

  @Override
  public String uri() {
    return URI;
  }

  @Override
  public Class<? extends HttpHandler> httpHandler() {
    return ProxyCacheHttpHandler.class;
  }
}
