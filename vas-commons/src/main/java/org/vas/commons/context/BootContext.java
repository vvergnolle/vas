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
 * Avoid method signature to change when we want to pass more or less informations
 * 
 * @see HttpHandlerPostProcessor
 */
public interface BootContext {
	
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
	 * The priority must be a valid positive integer that will prioritize your predicate.
	 * 
	 * @see PredicatedHandler
	 * @see Handlers#predicate(Predicate, HttpHandler, HttpHandler)
	 */
	void addPredicate(int priority, Predicate predicate, HttpHandler truePredicate);
	
	/**
	 * The same as {@link BootContext#addPredicate(Predicate, HttpHandler)} but the predicate will be wrapped by
	 * {@link Predicates#not(Predicate)}
	 * 
	 * @param predicate
	 * @param truePredicate
	 */
	void addNotPredicate(int priority, Predicate predicate, HttpHandler truePredicate);

	/**
	 * The application default web descriptor.
	 * 
	 * <br/>
	 * 
	 * For more informations on how to use it, look the Undertow <a href="http://undertow.io/documentation/index.html">documentation</a>.
	 * 
	 * @see Servlets
	 * @see DeploymentManager
	 * @see ServletInfo
	 * @see DeploymentInfo
	 */
	DeploymentInfo deploymentInfo();
}