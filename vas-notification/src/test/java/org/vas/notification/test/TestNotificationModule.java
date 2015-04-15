package org.vas.notification.test;

import java.util.Properties;

import org.vas.inject.ModuleDescriptor;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class TestNotificationModule extends AbstractModule implements ModuleDescriptor {

  @Override
  protected void configure() {
    bind(CountNotificationListener.class).asEagerSingleton();
  }

  @Override
  public Module module(Properties properties) {
    return this;
  }
}