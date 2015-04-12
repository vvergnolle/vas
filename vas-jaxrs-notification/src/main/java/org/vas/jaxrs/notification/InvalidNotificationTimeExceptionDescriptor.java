package org.vas.jaxrs.notification;

import java.util.function.Function;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.vas.jaxrs.JaxrsExceptionDescriptor;
import org.vas.jaxrs.notification.exception.InvalidNotificationTimeException;

public class InvalidNotificationTimeExceptionDescriptor implements JaxrsExceptionDescriptor,
  Function<Exception, ResponseBuilder> {

  @Override
  public Class<? extends Exception> exception() {
    return InvalidNotificationTimeException.class;
  }

  @Override
  public Function<Exception, ResponseBuilder> function() {
    return this;
  }

  @Override
  public ResponseBuilder apply(Exception e) {
    return Response.status(400).entity(e.getMessage());
  }
}
