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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.bean.MsgBean;

import com.google.common.collect.Maps;

public class RestImpl implements Rest {

	private static final int TIMEOUT = 4000;
	private static final String USER_AGENT = "Vas-Rest-Agent";
	protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final Map<String, String> defaultHeaders;

	public RestImpl() {
	  this(Maps.newHashMap());
  }

	public RestImpl(Map<String, String> defaultHeaders) {
	  super();
	  if(defaultHeaders == null) {
	  	this.defaultHeaders = Maps.newHashMap();
	  }
	  else {
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

	private String digestCredentials(String username,String password) {
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
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected Iterable<? extends NameValuePair> nameValuePairOf(Map<String, String> datas) {
		return datas
			.entrySet()
			.stream()
			.map(entry ->
				new BasicNameValuePair(entry.getKey(), entry.getValue())
			)
			.collect(Collectors.toList());
  }
	
	protected <T> T body(Class<T> klass, byte[] content) throws Exception {
		if(klass == String.class) {
			return content == null ? klass.cast("") : klass.cast(new String(content));
		} else if(klass == byte[].class) {
			return klass.cast(content);
		} else if(klass == Void.class) {
			return null;
		} else {
			try {
				return OBJECT_MAPPER.readValue(content, klass);
			}
			catch(JsonParseException | JsonMappingException e) {
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
	  request
	  	.connectTimeout(TIMEOUT)
			.socketTimeout(TIMEOUT)
			.userAgent(USER_AGENT);

		defaultHeaders
			.forEach((header, value) -> 
				request.addHeader(header, value));
  }

	protected <T> Response<T> extractResponse(Class<T> klass, org.apache.http.client.fluent.Response response)
      throws Exception {
	  HttpResponse httpResponse = response.returnResponse();
	  
	  byte[] content = null;
	  try {
	  	content = EntityUtils.toByteArray(httpResponse.getEntity());
	  }
	  catch(IllegalArgumentException e) {
	  	if(logger.isDebugEnabled()) {
	  		logger.debug("Error when reading entity", e);
	  	}
	  }

	  StatusLine statusLine = httpResponse.getStatusLine();
	  int status = statusLine.getStatusCode();

	  /* 
	   * Short circuit the content extraction if the 401 status has been returned - by default the response is in HTML
	   * for this status so the json parsing will fail.
	   * 
	   */
	  if(status == 401) {
	  	return Response.of(status, null, content);
	  }
	  
	  T body = body(klass, content);
	  if(body == null) {
	  	try {
	  		MsgBean msg = OBJECT_MAPPER.readValue(content, MsgBean.class);
	  		return Response.of(status, body, content, msg);
	  	}
	  	catch(Exception e) {
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
