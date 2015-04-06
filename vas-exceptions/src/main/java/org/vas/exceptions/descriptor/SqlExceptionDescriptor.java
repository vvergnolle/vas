package org.vas.exceptions.descriptor;

import java.sql.SQLException;
import java.util.function.Function;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.bean.MsgBean;
import org.vas.commons.messages.MessageHelper;
import org.vas.commons.messages.Messages;
import org.vas.jaxrs.JaxrsExceptionDescriptor;

public class SqlExceptionDescriptor implements JaxrsExceptionDescriptor, Function<Exception, ResponseBuilder> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  protected MessageHelper messageHelper;

  @Override
  public Class<? extends Exception> exception() {
    return SQLException.class;
  }

  @Override
  public Function<Exception, ResponseBuilder> function() {
    return this;
  }

  @Override
  public ResponseBuilder apply(Exception e) {
    if(logger.isErrorEnabled()) {
      logger.error("SQL exception", e);
    }

    int errorCode = ((SQLException) e).getErrorCode();
    return Response.status(500).entity(MsgBean.of(messageHelper.get(Messages.SQL_ERROR_OCCURED, errorCode)));
  }
}
