package org.vas.core.test;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;

public class TestModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(EventBus.class).asEagerSingleton();
    bind(CountEventListener.class).asEagerSingleton();
  }
}
