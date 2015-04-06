package org.vas.commons.http;

import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;

import org.vas.commons.context.BootContext;

/**
 * Invoke the {@link HttpHandlerPostProcessor#postProcess(BootContext)} method
 * when the default {@link PathHandler} is created
 * 
 */
public interface HttpHandlerPostProcessor {

  /**
   * Give a chance to some piece of code to play with the application
   * {@link PathHandler} and the default {@link DeploymentInfo} which in
   * Undertow, play the role of a web.xml.
   * 
   * <br/>
   * 
   * If you doesn't want to use the default {@link DeploymentInfo}, you can
   * create your own and bind it with the
   * {@link PathHandler#addPrefixPath(String, io.undertow.server.HttpHandler)}
   * or {@link PathHandler#addExactPath(String, io.undertow.server.HttpHandler)}
   * methods.
   * 
   * <br/>
   * <br/>
   * 
   * For more informations about Undertow and more precisely handlers, check the
   * <a href="http://undertow.io/documentation/index.html">documentation</a>.
   */
  void postProcess(BootContext context);
}
