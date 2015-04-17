/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.vas.core;

import java.util.Properties;

import org.vas.commons.messages.MessageHelper;
import org.vas.commons.utils.ShutdownRegistry;
import org.vas.inject.ModuleDescriptor;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class CoreModule extends AbstractModule implements ModuleDescriptor {

  private static final String LOCALE = "vas.locale";
  private static final String MESSAGES_LOCATION = "vas.messages";
  private static final String DEFAULT_MESSAGES = "locales/messages";

  protected Properties properties;

  @Override
  protected void configure() {
    bind(EventBus.class).asEagerSingleton();
    bind(ShutdownRegistry.class).asEagerSingleton();
    bind(MessageHelper.class).toInstance(createMessageHelper());
  }

  protected MessageHelper createMessageHelper() {
    String messagesLocation = properties.getProperty(MESSAGES_LOCATION, DEFAULT_MESSAGES);
    String preferredLocale = properties.getProperty(LOCALE);

    if(preferredLocale == null || preferredLocale.isEmpty()) {
      return new ResourceBundleMessageHelper(messagesLocation);
    } else {
      return new ResourceBundleMessageHelper(messagesLocation, preferredLocale);
    }
  }

  @Override
  public Module module(Properties properties) {
    this.properties = properties;
    return this;
  }
}
