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
package org.vas.notification;

import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.station.Autolib;
import org.vas.commons.station.LibStations;
import org.vas.commons.station.Stations;
import org.vas.commons.station.Velib;
import org.vas.commons.utils.GsonUtils;
import org.vas.domain.repository.Address;
import org.vas.domain.repository.User;
import org.vas.http.resource.HttpResponse;
import org.vas.inject.Services;
import org.vas.opendata.paris.client.AutolibOpendataParisWs;
import org.vas.opendata.paris.client.VelibOpendataParisWs;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

class Notifier {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected final Services services;
  protected AutolibOpendataParisWs autolibWs;
  protected VelibOpendataParisWs velibWs;
  // 1h cache
  protected final Cache<Integer, LibStations> cache = CacheBuilder.newBuilder().initialCapacity(50).softValues()
    .expireAfterWrite(1, TimeUnit.HOURS).build();
  protected final List<EntryDescriptor> descriptors = Lists.newArrayList();

  public Notifier(Services services) {
    super();
    this.services = services;
    this.autolibWs = services.get(AutolibOpendataParisWs.class);
    this.velibWs = services.get(VelibOpendataParisWs.class);
    init();
  }

  void init() {
    ServiceLoader.load(NotificationListenerDescriptor.class).forEach(
      descriptor -> descriptors.add(new EntryDescriptor(services.get(descriptor.listener()), descriptor)));
  }

  public void dispatch(User user, Address address, Notification notification) {
    descriptors.forEach(d -> doDispatch(user, address, notification, d));
  }

  protected void doDispatch(User user, Address address, Notification notification, EntryDescriptor descriptor) {
    if(logger.isTraceEnabled()) {
      logger.trace("Check if descriptor can handle notification of type {} - possible values {}", notification.type,
        descriptor.descriptor.types());
    }

    if(descriptor.descriptor.types().contains(notification.type)) {
      descriptor.listener.notify(user, address, notification, fetchLibStations(notification));

      if(logger.isDebugEnabled()) {
        logger.debug("Notification {} dispatched to listener {}", notification.id, descriptor.listener.getClass());
      }
    }
  }

  /**
   * Fetch a stations from WS or get a cached version
   */
  protected LibStations fetchLibStations(Notification notification) {
    LibStations libStations = cache.getIfPresent(notification.id);
    if(libStations == null) {
      libStations = doFetchLibStations(notification);
      cache.put(notification.id, libStations);
    }

    return libStations;
  }

  @SuppressWarnings("unchecked")
  protected LibStations doFetchLibStations(Notification notification) {
    HttpResponse autolibResponse = autolibWs.geofilter(notification.address.latitude, notification.address.longitude,
      notification.dist);
    Stations<Autolib> autolibs = GsonUtils.GSON.fromJson(new String(autolibResponse.bytes()), Stations.class);

    HttpResponse velibResponse = velibWs.geofilter(notification.address.latitude, notification.address.longitude,
      notification.dist);
    Stations<Velib> velibs = GsonUtils.GSON.fromJson(new String(velibResponse.bytes()), Stations.class);

    return new LibStations(autolibs, velibs);
  }

  protected class EntryDescriptor {
    public final NotificationListener listener;
    public final NotificationListenerDescriptor descriptor;

    public EntryDescriptor(NotificationListener listener, NotificationListenerDescriptor descriptor) {
      super();
      this.listener = listener;
      this.descriptor = descriptor;
    }
  }
}
