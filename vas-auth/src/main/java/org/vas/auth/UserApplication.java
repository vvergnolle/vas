package org.vas.auth;

import java.util.Arrays;
import java.util.List;

import org.vas.auth.resources.UserResource;
import org.vas.jaxrs.VasApplication;

public class UserApplication extends VasApplication {

  @Override
  protected List<Object> resources() {
    return Arrays.asList(new UserResource());
  }
}
