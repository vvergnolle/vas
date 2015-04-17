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
package org.vas.http.resource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.io.IOUtils;

public class HttpResource {

  protected String uri;
  protected Method method;
  protected String httpMethod;
  protected HttpEndpoint endpoint;
  protected HttpResourceInterceptor interceptor;

  public HttpResource(Method method, String uri, String httpMethod, HttpEndpoint endpoint,
    HttpResourceInterceptor interceptor) {
    super();
    this.uri = uri;
    this.method = method;
    this.endpoint = endpoint;
    this.httpMethod = httpMethod;
    this.interceptor = interceptor;
  }

  public HttpResponse makeRequest(Object... args) {
    String url = new StringBuilder(endpoint.baseUrl).append(uri).toString();

    if(args != null && args.length > 0) {
      url = String.format(Locale.ENGLISH, url, args);
    }

    if(interceptor != null) {
      final String passedUrl = url;
      return interceptor.intercept(method, url, () -> {
        return doMakeRequest(passedUrl, args);
      });
    } else {
      return doMakeRequest(url, args);
    }
  }

  protected HttpResponse doMakeRequest(String url, Object... args) {
    if("GET".equalsIgnoreCase(httpMethod)) {
      return doGetRequest(url, args);
    }

    throw new UnsupportedOperationException("Only GET is supported for now");
  }

  protected HttpResponse doGetRequest(String url, Object... args) {
    InputStream stream;
    try {
      stream = (InputStream) new URL(url).getContent();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      byte[] bytes = IOUtils.toByteArray(stream);
      return new HttpResponse(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(stream);
    }
  }
}
