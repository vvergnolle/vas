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

		if (args != null && args.length > 0) {
			url = String.format(Locale.ENGLISH, url, args);
		}

		if (interceptor != null) {
			final String passedUrl = url;
			return interceptor.intercept(method, url, () -> {
				return doMakeRequest(passedUrl, args);
			});
		} else {
			return doMakeRequest(url, args);
		}
	}

	protected HttpResponse doMakeRequest(String url, Object... args) {
		if (httpMethod.equalsIgnoreCase("GET")) {
			return doGETRequest(url, args);
		}

		throw new UnsupportedOperationException("Only GET is supported for now");
	}

	protected HttpResponse doGETRequest(String url, Object... args) {
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
