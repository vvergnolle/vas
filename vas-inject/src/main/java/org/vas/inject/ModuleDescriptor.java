package org.vas.inject;

import java.util.Properties;

import com.google.inject.Module;

public interface ModuleDescriptor {
	
	Module module(Properties properties);
}