package org.vas.jaxrs.notification;

import java.util.function.Function;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.vas.jaxrs.JaxrsExceptionDescriptor;
import org.vas.notification.exception.NotificationNotFoundException;

public class NotificationNotFoundExceptionDescriptor implements JaxrsExceptionDescriptor,
  Function<Exception, ResponseBuilder> {

  @Override
  public Class<? extends Exception> exception() {
    return NotificationNotFoundException.class;
  }

  @Override
  public Function<Exception, ResponseBuilder> function() {
    return this;
  }

  @Override
  public ResponseBuilder apply(Exception e) {
    return Response.status(204).entity(e.getMessage());
  }
}
