package org.vas.http.resource;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HttpEndpointFactory {
	
	public static <T> T createEndpoint(Class<T> klass) {
		return createEndpoint(klass, null);
	}
	
	public static <T> T createEndpoint(Class<T> klass, HttpResourceInterceptor interceptor) {
		org.vas.http.resource.annotation.HttpEndpoint aEndpoint = klass.getAnnotation(org.vas.http.resource.annotation.HttpEndpoint.class);
		if(aEndpoint == null) {
			throw new IllegalStateException("Http endpoint for " + klass + " must be setted");
		}
		
		HttpEndpoint endpoint = new HttpEndpoint(aEndpoint.value());
		for(Method method : klass.getMethods()) {
			org.vas.http.resource.annotation.HttpResource aResource = method.getAnnotation(org.vas.http.resource.annotation.HttpResource.class);
			if(aResource == null) {
				continue;
			}
			
			String uri = aResource.uri();
			String httpMethod = aResource.method();
			endpoint.addResource(method, new HttpResource(method, uri, httpMethod, endpoint, interceptor));
		}
		
		return klass.cast(
				Proxy.newProxyInstance(
						HttpEndpointFactory.class.getClassLoader(), new Class[] {klass}, new HttpResourceInvocationHandler(endpoint)));
	}
}
