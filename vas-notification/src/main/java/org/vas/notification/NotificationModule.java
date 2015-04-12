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
