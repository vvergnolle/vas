package org.vas.inject;

import org.vas.inject.guice.GuiceServiceContainer;

public final class ServiceContainers {

	private static final GuiceServiceContainer DEFAULT_SERVICE_CONTAINER = new GuiceServiceContainer();

	private ServiceContainers() {
  }
	
	public static ServiceContainer defaultContainer() {
		return DEFAULT_SERVICE_CONTAINER;
	}
}
