package org.vas.jaxrs.test;

import java.util.Arrays;
import java.util.List;

import org.vas.jaxrs.VasApplication;
import org.vas.jaxrs.test.resource.FooBarResource;

public class TestJaxrsApplication extends VasApplication {

	@Override
	protected List<Object> resources() {
	  return Arrays.asList(new FooBarResource());
	}
}