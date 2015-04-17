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
package org.vas.commons.context;

import io.undertow.Handlers;
import io.undertow.attribute.ExchangeAttribute;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.builder.PredicatedHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletInfo;

import java.util.Properties;

import org.vas.commons.http.HttpHandlerPostProcessor;

/**
 * Convenient holder to pass cross post processors
 * 
 * Avoid method signature to change when we want to pass more or less
 * informations
 * 
 * @see HttpHandlerPostProcessor
 */
public interface BootContext {

  /**
   * Bind an exception to an HTTP status. The exception message will not be
   * flushed in the response.
   */
  void bindException(Class<? extends Throwable> exception, int status);

  /**
   * Bind an exception to an HTTP status and force the exception message to
   * flushed in the response
   */
  void bindException(Class<? extends Throwable> exception, int status, boolean flushMessage);

  /**
   * Bind an exception to a function. Each time the exception will be raised,
   * the responseBody function will be invoked.
   */
  void bindException(Class<? extends Throwable> exception, HttpHandler handler);

  /**
   * Add this header for each request
   */
  void addHeader(String header, String value);

  /**
   * More dynamic way to add an header for each request
   */
  void addHeader(String header, ExchangeAttribute value);

  /**
   * The application properties
   */
  Properties properties();

  /**
   * Get a class that live in the underlaying services container
   */
  <T> T getService(Class<T> klass);

  /**
   * Inject dependencies of an instance
   */
  void inject(Object object);

  /**
   * Default http handler based on the uri
   */
  PathHandler pathHandler();

  /**
   * Register an handler with a predicate
   * 
   * <br/>
   * 
   * The priority must be a valid positive integer that will prioritize your
   * predicate.
   * 
   * @see PredicatedHandler
   * @see Handlers#predicate(Predicate, HttpHandler, HttpHandler)
   */
  void addPredicate(int priority, Predicate predicate, HttpHandler truePredicate);

  /**
   * The same as {@link BootContext#addPredicate(Predicate, HttpHandler)} but
   * the predicate will be wrapped by {@link Predicates#not(Predicate)}
   */
  void addNotPredicate(int priority, Predicate predicate, HttpHandler truePredicate);

  /**
   * The application default web descriptor.
   * 
   * <br/>
   * 
   * For more informations on how to use it, look the Undertow <a
   * href="http://undertow.io/documentation/index.html">documentation</a>.
   * 
   * @see Servlets
   * @see DeploymentManager
   * @see ServletInfo
   * @see DeploymentInfo
   */
  DeploymentInfo deploymentInfo();
}
