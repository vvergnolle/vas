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
