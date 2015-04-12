package org.vas.mail;

import java.util.Properties;

import org.vas.inject.ModuleDescriptor;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class MailModule extends AbstractModule implements ModuleDescriptor {

  protected Properties properties;

  @Override
  protected void configure() {
    bind(MailDispatcher.class).toInstance(new MailWorker(new Smtp(properties)));
    bind(MailEventListener.class).asEagerSingleton();
  }

  @Override
  public Module module(Properties properties) {
    this.properties = properties;
    return this;
  }
}
