package org.vas.inject;

import java.util.Properties;

public interface ServiceContainer {
	
	<T> T get(Class<T> klass);
	void inject(Object object);

	void init(Properties properties);
}