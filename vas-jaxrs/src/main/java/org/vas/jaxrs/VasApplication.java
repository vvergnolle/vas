package org.vas.jaxrs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.vas.inject.Services;
import org.vas.inject.ServicesUtil;
import org.vas.jaxrs.providers.SharedProviders;

public abstract class VasApplication extends Application {

  protected final Set<Object> singletons = new HashSet<>();

  {
    Services container = ServicesUtil.defaultContainer();
    resources().forEach((resource) -> {
      container.inject(resource);
      singletons.add(resource);
    });

    for (Class<?> klass : SharedProviders.CLASSES) {
      singletons.add(container.get(klass));
    }
  }

  protected abstract List<Object> resources();

  @Override
  public Set<Object> getSingletons() {
    return singletons;
  }
}
