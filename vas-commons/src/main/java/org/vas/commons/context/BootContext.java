package org.vas.commons.context;

import io.undertow.server.handlers.PathHandler;
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