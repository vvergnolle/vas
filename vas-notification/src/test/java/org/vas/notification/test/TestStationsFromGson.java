/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
