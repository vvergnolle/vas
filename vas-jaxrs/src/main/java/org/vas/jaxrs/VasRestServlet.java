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
package org.vas.jaxrs;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.wink.common.RuntimeContext;
import org.apache.wink.common.internal.lifecycle.LifecycleManager;
import org.apache.wink.common.internal.lifecycle.LifecycleManagerUtils;
import org.apache.wink.common.internal.lifecycle.ObjectCreationException;
import org.apache.wink.common.internal.lifecycle.ObjectFactory;
import org.apache.wink.server.internal.DeploymentConfiguration;
import org.apache.wink.server.internal.servlet.RestServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.inject.Services;
import org.vas.inject.ServicesUtil;
import org.vas.jaxrs.providers.SharedProviders;

import com.google.common.base.Preconditions;

/**
 * Override the apache wink rest servlet in order to provide DI with the
 * {@link Services} class.
 * 
 * This will the usage of {@link Inject} like annotations in jaxrs resources.
 * 
 */
public class VasRestServlet extends RestServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected DeploymentConfiguration getDeploymentConfiguration() throws ClassNotFoundException, InstantiationException,
    IllegalAccessException, IOException {
    Services services = ServicesUtil.defaultContainer();
    DeploymentConfiguration conf = super.getDeploymentConfiguration();
    conf.getOfFactoryRegistry().addFactoryFactory(new ServicesLifecycleManager<>(services));

    // Register custom providers
    for (Class<?> klass : SharedProviders.CLASSES) {
      conf.getProvidersRegistry().addProvider(services.get(klass));
    }

    return conf;
  }

  static class ServicesLifecycleManager<T> implements LifecycleManager<T> {

    private static final String ROOT_PCKG = "org.vas";

    final Logger logger = LoggerFactory.getLogger(getClass());
    final Services services;

    public ServicesLifecycleManager(Services services) {
      super();
      Preconditions.checkArgument(services != null, "Services shouldn't be null");
      this.services = services;
    }

    @Override
    public ObjectFactory<T> createObjectFactory(Class<T> object) throws ObjectCreationException {
      if(!object.getName().startsWith(ROOT_PCKG)) {
        return null;
      }

      if(logger.isTraceEnabled()) {
        logger.trace("Create wink object factory for {}", object);
      }

      boolean singleton = object.getAnnotation(Singleton.class) != null;
      if(!singleton) {
        singleton = object.getAnnotation(com.google.inject.Singleton.class) != null;
      }

      if(singleton) {
        return LifecycleManagerUtils.createSingletonObjectFactory(services.get(object));
      }

      return new PrototypeServicesObjectFactory<T>(object, services);
    }

    @Override
    public ObjectFactory<T> createObjectFactory(T object) throws ObjectCreationException {
      return LifecycleManagerUtils.createSingletonObjectFactory(object);
    }
  }

  static class PrototypeServicesObjectFactory<T> implements ObjectFactory<T> {

    final Logger logger = LoggerFactory.getLogger(getClass());

    final Class<T> klass;
    final Services services;
    final ObjectFactory<T> prototypeFactory;

    public PrototypeServicesObjectFactory(Class<T> klass, Services services) {
      super();
      this.klass = klass;
      this.services = services;
      this.prototypeFactory = LifecycleManagerUtils.createPrototypeObjectFactory(klass);
    }

    @Override
    public T getInstance(RuntimeContext context) {
      /*
       * Required in order to properly inject field with jaxrs annotations (like
       * javax.ws.rs.core.Context)
       */
      T instance = prototypeFactory.getInstance(context);
      services.inject(instance);
      return instance;
    }

    @Override
    public Class<T> getInstanceClass() {
      return prototypeFactory.getInstanceClass();
    }

    @Override
    public void releaseAll(RuntimeContext context) {
      prototypeFactory.releaseAll(context);
    }

    @Override
    public void releaseInstance(T instance, RuntimeContext context) {
      prototypeFactory.releaseInstance(instance, context);
    }
  }
}
