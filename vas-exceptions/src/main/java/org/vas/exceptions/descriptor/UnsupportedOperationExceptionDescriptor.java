package org.vas.exceptions.descriptor;

import java.util.function.Function;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.vas.commons.bean.MsgBean;
import org.vas.jaxrs.JaxrsExceptionDescriptor;

public class UnsupportedOperationExceptionDescriptor implements JaxrsExceptionDescriptor,
    Function<Exception, ResponseBuilder> {

	@Override
	public Class<? extends Exception> exception() {
		return UnsupportedOperationException.class;
	}

	@Override
	public Function<Exception, ResponseBuilder> function() {
		return this;
	}

	@Override
	public ResponseBuilder apply(Exception t) {
		return Response
			.status(501)
			.entity(MsgBean.of(t.getMessage()));
	}
}
