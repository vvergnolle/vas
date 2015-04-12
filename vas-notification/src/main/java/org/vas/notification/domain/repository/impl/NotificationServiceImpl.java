package org.vas.notification.domain.repository.impl;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.vas.domain.repository.Address;
import org.vas.domain.repository.exception.DomainRepositoryException;
import org.vas.notification.Notification;
import org.vas.notification.domain.repository.NotificationRepository;
import org.vas.notification.domain.repository.NotificationService;
import org.vas.notification.exception.NotificationNotFoundException;

public class NotificationServiceImpl implements NotificationService {

  @Inject
  protected NotificationRepository repository;

  @Override
  public Notification fetch(int id) {
    try {
      Notification notification = repository.queryForId(id);
      if(notification == null) {
        throw new NotificationNotFoundException("Notification " + id + " not found");
      }

      return notification;
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }

  @Override
  public void remove(int id) {
    try {
      if(repository.deleteById(id) < 1) {
        throw new NotificationNotFoundException("Notification " + id + " not found");
      }
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }

  @Override
  public void save(Notification notification) {
    try {
      if(notification.id > 0) {
        repository.update(notification);
      } else {
        repository.create(notification);
      }
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }

  @Override
  public List<Notification> listByAddress(Address address) {
    try {
      return repository.queryBuilder().where().eq("address_id", address).query();
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }
  }
}
