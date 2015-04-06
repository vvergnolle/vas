package org.vas.jaxrs.address.exception;

import static javax.ws.rs.core.Response.noContent;

import java.util.function.Function;

import javax.ws.rs.core.Response.ResponseBuilder;

import org.vas.domain.repository.exception.AddressNotFoundException;
import org.vas.jaxrs.JaxrsExceptionDescriptor;

public class AddressNotFoundExceptionDescriptor implements JaxrsExceptionDescriptor {

  @Override
  public Class<? extends Exception> exception() {
    return AddressNotFoundException.class;
  }

  @Override
  public Function<Exception, ResponseBuilder> function() {
    return (e) -> noContent();
  }
}
