package org.vas.auth;

import io.undertow.security.idm.IdentityManager;

import java.util.Properties;

import org.vas.inject.guice.GuiceModuleDescriptor;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class AuthModule extends AbstractModule implements GuiceModuleDescriptor {

	@Override
	protected void configure() {
		bind(IdentityManager.class).to(RepositoryIdentityManager.class);
	}

	@Override
  public Module module(Properties properties) {
	  return this;
  }
}
