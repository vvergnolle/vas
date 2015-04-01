package org.vas.http.resource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HttpResourceInvocationHandler implements InvocationHandler {

	protected static HttpConverterRepository converterRepository = new HttpConverterRepository();

	protected HttpEndpoint enpoint;

	public HttpResourceInvocationHandler(HttpEndpoint enpoint) {
		super();
		this.enpoint = enpoint;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		HttpResource resource = enpoint.lookupResource(method);
		HttpResponse response = resource.makeRequest(args);
		HttpResponseConverter<? super Object> converter = converterRepository.lookup(resource, method.getReturnType());
		
		if(converter != null) {
			return converter.apply(response);
		}
		else {
			return response;
		}
	}
}
