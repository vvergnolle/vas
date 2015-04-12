package org.vas.worker;

import java.util.Properties;

import org.vas.inject.ModuleDescriptor;
import org.vas.worker.impl.WorkerServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class WorkerModule extends AbstractModule implements ModuleDescriptor {

  @Override
  protected void configure() {
    bind(WorkerService.class).to(WorkerServiceImpl.class).asEagerSingleton();
  }

  @Override
  public Module module(Properties properties) {
    return this;
  }
}
