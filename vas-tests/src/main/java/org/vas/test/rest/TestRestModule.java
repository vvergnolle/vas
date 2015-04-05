package org.vas.test.rest;

import com.google.inject.AbstractModule;

public class TestRestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(VasRest.class).to(VasRestImpl.class);
		bind(Rest.class).to(RestImpl.class);
	}
}
