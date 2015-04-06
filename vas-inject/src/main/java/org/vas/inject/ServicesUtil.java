package org.vas.inject;

import org.vas.inject.impl.ServicesImpl;

public final class ServicesUtil {

  private static final ServicesImpl DEFAULT_SERVICE_CONTAINER = new ServicesImpl();

  private ServicesUtil() {}

  public static Services defaultContainer() {
    return DEFAULT_SERVICE_CONTAINER;
  }
}
