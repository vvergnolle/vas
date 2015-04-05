package org.vas.inject;

import java.util.List;
import java.util.Properties;

import com.google.inject.Module;

public interface Services {

	<T> T get(Class<T> klass);
	void inject(Object object);

	Services child(Module... modules);
	Services child(Iterable<Module> modules);

	void init(Properties properties);
	List<Module> modules();
}
