package org.vas.inject.guice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

import org.vas.inject.ServiceContainer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceServiceContainer implements ServiceContainer {

	protected Injector injector;
	
	@Override
	public void init(Properties properties) {
		List<Module> modules = new ArrayList<>();
		Iterator<GuiceModuleDescriptor> moduleDescriptors = ServiceLoader.load(GuiceModuleDescriptor.class).iterator();
		moduleDescriptors.forEachRemaining(descriptor -> modules.add(descriptor.module(properties)));
		
		injector = Guice.createInjector(modules);
	}
	
	@Override
	public <T> T get(Class<T> klass) {
	  return injector.getInstance(klass);
	}
	
	@Override
	public void inject(Object object) {
		injector.injectMembers(object);
	}
}