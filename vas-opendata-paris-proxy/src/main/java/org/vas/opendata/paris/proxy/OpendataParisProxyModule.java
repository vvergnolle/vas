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
