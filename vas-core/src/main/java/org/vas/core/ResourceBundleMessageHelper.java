package org.vas.core;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.vas.commons.messages.MessageHelper;

public class ResourceBundleMessageHelper implements MessageHelper {

	protected final String messagesLocation;
	protected final ResourceBundle resourceBundle;

	public ResourceBundleMessageHelper(String messagesLocation) {
	  this(messagesLocation, Locale.getDefault());
  }
	
	public ResourceBundleMessageHelper(String messagesLocation, String lang) {
	  this(messagesLocation, new Locale(lang));
  }
	
	public ResourceBundleMessageHelper(String messagesLocation, Locale locale) {
	  super();
	  this.messagesLocation = messagesLocation;
	  this.resourceBundle = ResourceBundle.getBundle(messagesLocation, locale);
  }
	
	@Override
	public MessageHelper switchTo(String lang) {
	  return new ResourceBundleMessageHelper(messagesLocation, lang);
	}
	
	@Override
	public String get(String key, Object... params) {
		String msg = resourceBundle.getString(key);

		if(params != null && params.length > 0) {
			return MessageFormat.format(msg, params);
		}

	  return msg;
	}
}
