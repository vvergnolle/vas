package org.vas.commons.http;

import io.undertow.server.HttpHandler;

/**
 * Describe an {@link HttpHandler} class that live in the classpath
 * 
 */
public interface HttpHandlerDescriptor {

  /**
   * The uri that will be bind to your handler
   */
  String uri();

  /**
   * The handler class
   */
  Class<? extends HttpHandler> httpHandler();
}
