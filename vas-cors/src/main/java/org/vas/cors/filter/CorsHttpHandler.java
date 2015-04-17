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
    if(methods != null) {
      httpHeaders.addFirst(new HttpString("Access-Control-Allow-Methods"), methods);
    }

    String headers = requestHeaders.getFirst("Access-Control-Request-Headers");
    if(headers != null) {
      httpHeaders.addFirst(new HttpString("Access-Control-Allow-Headers"), headers);
    }

    httpHeaders.addFirst(new HttpString("Access-Control-Allow-Credentials"), "true");
  }
}
