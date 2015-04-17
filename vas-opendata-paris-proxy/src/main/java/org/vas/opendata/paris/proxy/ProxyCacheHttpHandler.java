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
package org.vas.opendata.paris.proxy;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.vas.inject.ServicesUtil;
import org.vas.opendata.paris.client.AutolibOpendataParisWs;
import org.vas.opendata.paris.client.VelibOpendataParisWs;

public class ProxyCacheHttpHandler implements HttpHandler {

  private static final String VELIB_SERVICE = "velib";
  private static final String AUTOLIB_SERVICE = "autolib";
  private static final byte[] EMPTY_BYTES = new byte[0];

  protected Pattern pattern;
  protected VelibOpendataParisWs velibWs;
  protected AutolibOpendataParisWs autolibWs;

  public ProxyCacheHttpHandler() {
    super();
    pattern = Pattern.compile("/ws/opendata/(velib|autolib)/([\\d]{1,2}\\.[\\d]+)/([\\d]{1,2}\\.[\\d]+)/([\\d]{1,4})");
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) throws Exception {
    if(exchange.isInIoThread()) {
      exchange.dispatch(this);
      return;
    }

    String key = exchange.getRequestURI();
    Matcher matcher = pattern.matcher(key);

    if(!matcher.matches()) {
      exchange.setResponseCode(404);
      exchange.endExchange();
      return;
    }

    String service = matcher.group(1);
    float lat = Float.valueOf(matcher.group(2));
    float lng = Float.valueOf(matcher.group(3));
    short dist = Short.valueOf(matcher.group(4));

    byte[] bytes = null;
    if(service.equals(AUTOLIB_SERVICE)) {
      bytes = autolibWs().geofilter(lat, lng, dist).bytes();
    } else if(service.equals(VELIB_SERVICE)) {
      bytes = velibWs().geofilter(lat, lng, dist).bytes();
    } else {
      bytes = EMPTY_BYTES;
    }

    exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
  }

  protected VelibOpendataParisWs velibWs() {
    if(velibWs == null) {
      velibWs = ServicesUtil.defaultContainer().get(VelibOpendataParisWs.class);
    }

    return velibWs;
  }

  private AutolibOpendataParisWs autolibWs() {
    if(autolibWs == null) {
      autolibWs = ServicesUtil.defaultContainer().get(AutolibOpendataParisWs.class);
    }

    return autolibWs;
  }
}
