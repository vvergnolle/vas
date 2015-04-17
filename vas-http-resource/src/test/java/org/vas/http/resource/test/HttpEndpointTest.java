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
package org.vas.http.resource.test;

import org.testng.annotations.Test;
import org.vas.http.resource.HttpEndpointFactory;
import org.vas.http.resource.HttpResponse;
import org.vas.http.resource.annotation.HttpEndpoint;
import org.vas.http.resource.annotation.HttpResource;

public class HttpEndpointTest {

  @HttpEndpoint("http://opendata.paris.fr/explore/dataset/stations-velib-disponibilites-en-temps-reel")
  interface OpenDataParisWS {

    @HttpResource(uri = "?tab=api&q=&geofilter.distance=48.857924399999995,2.402112,1000")
    HttpResponse raw();
  }

  @Test
  public void itShouldMakeGetRequest() {
    OpenDataParisWS ws = HttpEndpointFactory.createEndpoint(OpenDataParisWS.class);
    ws.raw();
  }
}
