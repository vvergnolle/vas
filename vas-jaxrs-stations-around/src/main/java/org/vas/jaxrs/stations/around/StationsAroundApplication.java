package org.vas.jaxrs.stations.around;

import java.util.Arrays;
import java.util.List;

import org.vas.jaxrs.VasApplication;
import org.vas.jaxrs.stations.around.resource.StationsAroundResource;

public class StationsAroundApplication extends VasApplication {

  @Override
  protected List<Object> resources() {
    return Arrays.asList(new StationsAroundResource());
  }
}
