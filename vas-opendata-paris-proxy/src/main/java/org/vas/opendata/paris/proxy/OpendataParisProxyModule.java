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
package org.vas.opendata.paris.proxy;

import static org.vas.http.resource.HttpEndpointFactory.createEndpoint;

import java.util.Properties;

import org.vas.inject.ModuleDescriptor;
import org.vas.opendata.paris.client.AutolibOpendataParisWs;
import org.vas.opendata.paris.client.VelibOpendataParisWs;
import org.vas.opendata.paris.proxy.interceptor.AutolibCacheInterceptor;
import org.vas.opendata.paris.proxy.interceptor.VelibCacheInterceptor;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class OpendataParisProxyModule extends AbstractModule implements ModuleDescriptor {

  @Override
  protected void configure() {
    bind(VelibOpendataParisWs.class)
      .toInstance(createEndpoint(VelibOpendataParisWs.class, new VelibCacheInterceptor()));
    bind(AutolibOpendataParisWs.class).toInstance(
      createEndpoint(AutolibOpendataParisWs.class, new AutolibCacheInterceptor()));
  }

  @Override
  public Module module(Properties properties) {
    return this;
  }
}
