/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
