package org.vas.jaxrs.providers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.function.Function;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.bean.MsgBean;
import org.vas.inject.Services;
import org.vas.inject.ServicesUtil;
import org.vas.jaxrs.JaxrsExceptionDescriptor;

@Provider
public class JaxrsExceptionProvider implements ExceptionMapper<Exception> {

	public static final JaxrsExceptionProvider INSTANCE = new JaxrsExceptionProvider();

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final Map<Class<? extends Exception>, Function<Exception, ResponseBuilder>> descriptors;

	{
		Services container = ServicesUtil.defaultContainer();
		Map<Class<? extends Exception>, Function<Exception, ResponseBuilder>> jaxrsDescriptors = new HashMap<>();

		ServiceLoader
			.load(JaxrsExceptionDescriptor.class)
			.iterator()
			.forEachRemaining((d) -> {
				Function<Exception, ResponseBuilder> function = d.function();
				container.inject(function);

				jaxrsDescriptors.put(d.exception(), function);
			});

		descriptors = Collections.unmodifiableMap(jaxrsDescriptors);
	}

	@Override
  public Response toResponse(Exception e) {
		Class<? extends Exception> klass = e.getClass();

		for(Entry<Class<? extends Exception>, Function<Exception, ResponseBuilder>> entry : descriptors.entrySet()) {
			Class<? extends Exception> descriptorExceptionClass = entry.getKey();

			if(descriptorExceptionClass == klass || descriptorExceptionClass.isAssignableFrom(klass)) {
				if(logger.isTraceEnabled()) {
					logger.trace("Found exception mapper {}", descriptorExceptionClass);
				}

				ResponseBuilder response = entry.getValue().apply(e);
				response.type(MediaType.APPLICATION_JSON);
				return response.build();
			}
		}
		
		/*
		 * Check Wink WebApplicationException that already provide a Response 
		 */
		if(e instanceof WebApplicationException) {
			return ((WebApplicationException) e).getResponse();
		}

		if(logger.isErrorEnabled()) {
			logger.error("Unhandled exception", e);
		}
		
	  return Response
	  	.serverError()
	  	.entity(MsgBean.of(e.getMessage()))
	  	.type(MediaType.APPLICATION_JSON)
	  	.build();
  }
}