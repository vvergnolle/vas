package org.vas.jaxrs;

import java.util.Properties;

import org.vas.inject.guice.GuiceModuleDescriptor;
import org.vas.jaxrs.providers.JaxrsExceptionProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class JaxrsModule extends AbstractModule implements GuiceModuleDescriptor {

	@Override
	protected void configure() {
	  bind(JaxrsExceptionProvider.class);
	}

	@Override
	public Module module(Properties properties) {
	  return this;
	}
}