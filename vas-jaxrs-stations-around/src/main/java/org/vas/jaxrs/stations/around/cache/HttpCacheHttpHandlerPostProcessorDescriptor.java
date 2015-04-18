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
package org.vas.jaxrs.stations.around.cache;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.util.ImmediateInstanceFactory;

import java.util.Properties;

import javax.servlet.DispatcherType;

import org.vas.commons.context.BootContext;
import org.vas.commons.http.HttpHandlerPostProcessor;
import org.vas.http.resource.cache.EtagCache;
import org.vas.jaxrs.cache.CacheFilter;
import org.vas.jaxrs.stations.around.StationsAroundJaxrsDescriptor;

public class HttpCacheHttpHandlerPostProcessorDescriptor implements HttpHandlerPostProcessor {

  static final String FILTER_NAME = "stations-around-cache-filter";

  @Override
  public void postProcess(BootContext context) {
    CachePolicy cachePolicy = new CachePolicy(context.properties());
    if(!cachePolicy.enabled) {
      return;
    }

    EtagCache etagCache = context.getService(EtagCache.class);
    InstanceFactory<CacheFilter> instanceFactory = new ImmediateInstanceFactory<>(new CacheFilter(etagCache));
    FilterInfo filterInfo = Servlets.filter(FILTER_NAME, CacheFilter.class, instanceFactory);
    context.deploymentInfo().addFilter(filterInfo);
    context.deploymentInfo().addFilterUrlMapping(FILTER_NAME, StationsAroundJaxrsDescriptor.MAPPING,
      DispatcherType.REQUEST);
  }

  /**
   * Cache filter policy
   */
  static class CachePolicy {

    static final String ENABLED = "vas.resource.stations.around.cache.enabled";

    boolean enabled;

    public CachePolicy(Properties properties) {
      super();
      // Default is enabled
      enabled = Boolean.valueOf(properties.getProperty(ENABLED, "true"));
    }
  }
}
