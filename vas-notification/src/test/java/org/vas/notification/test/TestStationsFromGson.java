package org.vas.notification.test;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.vas.commons.station.Autolib;
import org.vas.commons.station.Stations;
import org.vas.commons.station.Velib;
import org.vas.commons.utils.GsonUtils;

public class TestStationsFromGson {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Test
  @SuppressWarnings("unchecked")
  public void itShouldReadStaticVelibs() throws IOException {
    byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/velibs.json"));
    Stations<Velib> velibs = GsonUtils.GSON.fromJson(new String(bytes), Stations.class);

    if(logger.isInfoEnabled()) {
      logger.info("Velibs {}", velibs.getStations());
    }

    Assertions.assertThat(velibs.getStations()).as("Velibs stations shouldn't be empty").isNotEmpty();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void itShouldReadStaticAutolibs() throws IOException {
    byte[] bytes = IOUtils.toByteArray(getClass().getResourceAsStream("/autolibs.json"));
    Stations<Autolib> autolibs = GsonUtils.GSON.fromJson(new String(bytes), Stations.class);

    if(logger.isInfoEnabled()) {
      logger.info("Autolibs {}", autolibs.getStations());
    }

    Assertions.assertThat(autolibs.getStations()).as("Autolibs stations shouldn't be empty").isNotEmpty();
  }
}
