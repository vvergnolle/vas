package org.vas.commons.utils;

import io.undertow.server.HttpServerExchange;

public class ExceptionContext {

  public final Throwable exception;
  public final HttpServerExchange exchange;

  public ExceptionContext(Throwable exception, HttpServerExchange exchange) {
    super();
    this.exception = exception;
    this.exchange = exchange;
  }

  public static ExceptionContext of(Throwable e, HttpServerExchange exchange) {
    return new ExceptionContext(e, exchange);
  }
}
