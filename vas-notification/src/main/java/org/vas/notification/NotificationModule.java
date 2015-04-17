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
package org.vas.notification;

import java.sql.SQLException;
import java.util.Properties;

import org.vas.domain.repository.exception.DomainRepositoryException;
import org.vas.inject.ModuleDescriptor;
import org.vas.notification.domain.repository.NotificationRepository;
import org.vas.notification.domain.repository.NotificationService;
import org.vas.notification.domain.repository.impl.NotificationServiceImpl;
import org.vas.notification.listener.StartEventListener;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class NotificationModule extends AbstractModule implements ModuleDescriptor {

  private Properties properties;

  @Override
  protected void configure() {
    bind(StartEventListener.class).toInstance(new StartEventListener(properties));
    bind(NotificationService.class).to(NotificationServiceImpl.class);
  }

  @Provides
  public NotificationRepository notificationRepository(ConnectionSource source) {
    try {
      return (NotificationRepository) DaoManager.createDao(source, Notification.class);
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }

  @Override
  public Module module(Properties properties) {
    this.properties = properties;
    return this;
  }
}
