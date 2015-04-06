package org.vas.exceptions.test;

import java.util.Arrays;
import java.util.List;

import org.vas.exceptions.test.resources.NopResource;
import org.vas.jaxrs.VasApplication;

public class TestExceptionsApplication extends VasApplication {

  @Override
  protected List<Object> resources() {
    return Arrays.asList(new NopResource());
  }
}
