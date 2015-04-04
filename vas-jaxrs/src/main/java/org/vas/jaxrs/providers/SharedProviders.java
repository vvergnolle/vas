package org.vas.jaxrs.providers;

import java.util.ArrayList;
import java.util.List;

import org.vas.jaxrs.VasApplication;

/**
 * Providers that are located in the {@link SharedProviders#LIST} constant will
 * be automatically inserted as singletons in {@link VasApplication}
 * applications.
 * 
 */
public class SharedProviders {

	public static List<Class<?>> CLASSES = new ArrayList<>(1);

	static {
		CLASSES.add(JaxrsExceptionProvider.class);
	}
}
