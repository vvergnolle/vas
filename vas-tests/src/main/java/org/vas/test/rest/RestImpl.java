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
package org.vas.test.rest;

import io.undertow.util.Headers;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.bean.MsgBean;
import org.vas.commons.utils.GsonUtils;

import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;

public class RestImpl implements Rest {

  private static final int TIMEOUT = 4000;
  private static final String USER_AGENT = "Vas-Rest-Agent";

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected final Map<String, String> defaultHeaders;

  public RestImpl() {
    this(Maps.newHashMap());
  }

  public RestImpl(Map<String, String> defaultHeaders) {
    super();
    if(defaultHeaders == null) {
      this.defaultHeaders = Maps.newHashMap();
    } else {
      this.defaultHeaders = defaultHeaders;
    }

    if(!this.defaultHeaders.containsKey(HttpHeaders.ALLOW)) {
      this.defaultHeaders.put(HttpHeaders.ALLOW, ContentType.APPLICATION_JSON.getMimeType());
    }
  }

  @Override
  public Rest withBasic(String username, String password) {
    Map<String, String> map = Maps.newHashMap();
    map.put(Headers.AUTHORIZATION_STRING, digestCredentials(username, password));
    map.putAll(defaultHeaders);

    return createRestBasicInstance(map);
  }

  private String digestCredentials(String username, String password) {
    String credential = username + ":" + password;
    String digest = Base64.getEncoder().encodeToString(credential.getBytes());
    return "Basic " + digest;
  }

  @Override
  public <T> Response<T> get(String uri, Class<T> klass, Object... args) {
    uri = formattedUri(uri, args);

    try {
      org.apache.http.client.fluent.Response response = httpGet(uri).execute();
      return extractResponse(klass, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> Response<T> post(String uri, Class<T> klass, Object... args) {
    return post(uri, Collections.emptyMap(), klass, args);
  }

  @Override
  public <T> Response<T> post(String uri, Map<String, String> datas, Class<T> klass, Object... args) {
    uri = formattedUri(uri, args);

    Request request = httpPost(uri);
    request.bodyForm(nameValuePairOf(datas));

    try {
      org.apache.http.client.fluent.Response response = request.execute();
      return extractResponse(klass, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> Response<T> put(String uri, Class<T> klass, Object... args) {
    return put(uri, Collections.emptyMap(), klass, args);
  }

  @Override
  public <T> Response<T> put(String uri, Map<String, String> datas, Class<T> klass, Object... args) {
    uri = formattedUri(uri, args);

    Request request = httpPut(uri);
    request.bodyForm(nameValuePairOf(datas));

    try {
      org.apache.http.client.fluent.Response response = request.execute();
      return extractResponse(klass, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> Response<T> delete(String uri, Class<T> klass, Object... args) {
    uri = formattedUri(uri, args);

    Request request = httpDelete(uri);
    try {
      org.apache.http.client.fluent.Response response = request.execute();
      return extractResponse(klass, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected Iterable<? extends NameValuePair> nameValuePairOf(Map<String, String> datas) {
    return datas.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
      .collect(Collectors.toList());
  }

  protected <T> T lookupBody(Class<T> klass, byte[] content) throws Exception {
    if(klass == String.class) {
      return content == null ? klass.cast("") : klass.cast(new String(content));
    } else if(klass == byte[].class) {
      return klass.cast(content);
    } else if(klass == Void.class) {
      return null;
    } else {
      try {
        return GsonUtils.GSON.fromJson(new String(content), klass);
      } catch (JsonSyntaxException e) {
        if(logger.isWarnEnabled()) {
          logger.warn("Fail to parse json", e);
        }
        return null;
      }
    }
  }

  protected Request httpGet(String uri) {
    Request request = Request.Get(uri);
    configureRequest(request);

    return request;
  }

  protected Request httpPost(String uri) {
    Request request = Request.Post(uri);
    configureRequest(request);

    return request;
  }

  protected Request httpPut(String uri) {
    Request request = Request.Put(uri);
    configureRequest(request);

    return request;
  }

  protected Request httpDelete(String uri) {
    Request request = Request.Delete(uri);
    configureRequest(request);

    return request;
  }

  protected void configureRequest(Request request) {
    request.connectTimeout(TIMEOUT).socketTimeout(TIMEOUT).userAgent(USER_AGENT);

    defaultHeaders.forEach((header, value) -> request.addHeader(header, value));
  }

  protected <T> Response<T> extractResponse(Class<T> klass, org.apache.http.client.fluent.Response response)
    throws Exception {
    HttpResponse httpResponse = response.returnResponse();

    byte[] content = null;
    try {
      content = EntityUtils.toByteArray(httpResponse.getEntity());
    } catch (IllegalArgumentException e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Error when reading entity", e);
      }
    }

    StatusLine statusLine = httpResponse.getStatusLine();
    int status = statusLine.getStatusCode();

    /*
     * Short circuit the content extraction if the 401 status has been returned
     * - by default the response is in HTML for this status so the json parsing
     * will fail.
     */
    if(status == 401) {
      return Response.of(status, null, content);
    } else if(status == 204) {
      return Response.of(status, null, content);
    }

    T body = lookupBody(klass, content);
    if(body == null) {
      try {
        MsgBean msg = GsonUtils.GSON.fromJson(new String(content), MsgBean.class);
        return Response.of(status, body, content, msg);
      } catch (JsonSyntaxException e) {
        logger.warn("Fail to fallback on a MsgBean body", e);
      }
    }

    return Response.of(statusLine.getStatusCode(), body, content);
  }

  protected String formattedUri(String uri, Object... args) {
    if(args != null && args.length > 0) {
      uri = MessageFormat.format(uri, args);
    }

    return uri;
  }

  protected RestImpl createRestBasicInstance(Map<String, String> map) {
    return new RestImpl(map);
  }
}
