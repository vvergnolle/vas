package org.vas.exceptions;

import org.vas.commons.context.BootContext;
import org.vas.commons.http.HttpHandlerPostProcessor;

public class ExceptionsHandlerPostProcessor implements HttpHandlerPostProcessor {

  @Override
  public void postProcess(BootContext context) {
    context.bindException(Exception.class, 500, true);
  }
}
