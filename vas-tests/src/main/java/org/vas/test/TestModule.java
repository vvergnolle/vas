package org.vas.test;

import org.vas.launcher.Env;
import org.vas.launcher.ServerConf;
import org.vas.launcher.Vas;

import com.google.inject.AbstractModule;

public class TestModule extends AbstractModule {

  protected Vas vas;

  public TestModule(Vas vas) {
    super();
    this.vas = vas;
  }

  @Override
  protected void configure() {
    bind(Vas.class).toInstance(vas);
    bind(Env.class).toInstance(vas.env());
    bind(ServerConf.class).toInstance(vas.conf());
  }
}
