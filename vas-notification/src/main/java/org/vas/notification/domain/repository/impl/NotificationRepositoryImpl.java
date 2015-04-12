package org.vas.notification.domain.repository.impl;

import java.sql.SQLException;

import org.vas.notification.Notification;
import org.vas.notification.domain.repository.NotificationRepository;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class NotificationRepositoryImpl extends BaseDaoImpl<Notification, Integer> implements NotificationRepository {

  public NotificationRepositoryImpl(Class<Notification> dataClass) throws SQLException {
    super(dataClass);
  }

  public NotificationRepositoryImpl(ConnectionSource connectionSource, Class<Notification> dataClass)
    throws SQLException {
    super(connectionSource, dataClass);
  }

  public NotificationRepositoryImpl(ConnectionSource connectionSource, DatabaseTableConfig<Notification> tableConfig)
    throws SQLException {
    super(connectionSource, tableConfig);
  }
}
