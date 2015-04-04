package org.vas.jaxrs;

import java.util.function.Function;

import javax.ws.rs.core.Response.ResponseBuilder;

public interface JaxrsExceptionDescriptor {

	Class<? extends Exception> exception();

	Function<Exception, ResponseBuilder> function();
}