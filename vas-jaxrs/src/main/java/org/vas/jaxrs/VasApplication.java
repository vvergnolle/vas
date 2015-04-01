package org.vas.jaxrs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.vas.inject.ServiceContainer;
import org.vas.inject.ServiceContainers;

public abstract class VasApplication extends Application {

	protected final Set<Object> singletons = new HashSet<>();
	
	protected abstract List<Object> resources();

	{
		ServiceContainer container = ServiceContainers.defaultContainer();
		resources().forEach((resource) -> {
			container.inject(resource);
			singletons.add(resource);
		});
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
