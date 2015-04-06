package org.vas.inject.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

import org.vas.inject.ModuleDescriptor;
import org.vas.inject.Services;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ServicesImpl implements Services {

  protected Injector injector;
  protected List<Module> modules;

  public ServicesImpl() {
    super();
  }

  public ServicesImpl(Injector injector, List<Module> modules) {
    super();
    this.injector = injector;
    this.modules = modules;
  }

  @Override
  public void init(Properties properties) {
    if(injector != null) {
      return;
    }

    modules = new ArrayList<>();
    Iterator<ModuleDescriptor> moduleDescriptors = ServiceLoader.load(ModuleDescriptor.class).iterator();
    moduleDescriptors.forEachRemaining(descriptor -> modules.add(descriptor.module(properties)));

    injector = Guice.createInjector(modules);
  }

  @Override
  public Services child(Module... modules) {
    Injector childInjector = injector.createChildInjector(modules);
    return new ServicesImpl(childInjector, Lists.newArrayList(modules));
  }

  @Override
  public Services child(Iterable<Module> modules) {
    Injector childInjector = injector.createChildInjector(modules);
    return new ServicesImpl(childInjector, Lists.newArrayList(modules));
  }

  @Override
  public List<Module> modules() {
    return Collections.unmodifiableList(modules);
  }

  @Override
  public <T> T get(Class<T> klass) {
    return injector.getInstance(klass);
  }

  @Override
  public void inject(Object object) {
    injector.injectMembers(object);
  }
}
