package org.vas.jaxrs.address;

import java.util.Arrays;
import java.util.List;

import org.vas.jaxrs.VasApplication;
import org.vas.jaxrs.address.resource.AddressResource;

public class AddressApplication extends VasApplication {

  @Override
  protected List<Object> resources() {
    return Arrays.asList(new AddressResource());
  }
}
