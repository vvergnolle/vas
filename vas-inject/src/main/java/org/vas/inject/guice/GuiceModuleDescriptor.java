package org.vas.inject.guice;

import java.util.Properties;

import com.google.inject.Module;

public interface GuiceModuleDescriptor {
	
	Module module(Properties properties);
}