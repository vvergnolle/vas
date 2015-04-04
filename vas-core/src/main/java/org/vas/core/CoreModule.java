package org.vas.core;

import java.util.Properties;

import org.vas.commons.messages.MessageHelper;
import org.vas.inject.guice.GuiceModuleDescriptor;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class CoreModule extends AbstractModule implements GuiceModuleDescriptor {

	private static final String LOCALE = "vas.locale";
	private static final String MESSAGES_LOCATION = "vas.messages";
	private static final String DEFAULT_MESSAGES = "locales/messages";

	protected Properties properties;

	@Override
	protected void configure() {
		bind(MessageHelper.class).toInstance(createMessageHelper());
	}

	protected MessageHelper createMessageHelper() {
		String messagesLocation = properties.getProperty(MESSAGES_LOCATION, DEFAULT_MESSAGES);
		String preferredLocale = properties.getProperty(LOCALE);

		if(preferredLocale == null || preferredLocale.isEmpty()) {
			return new ResourceBundleMessageHelper(messagesLocation);
		}
		else {
			return new ResourceBundleMessageHelper(messagesLocation, preferredLocale);
		}
	}

	@Override
	public Module module(Properties properties) {
		this.properties = properties;
		return this;
	}
}
