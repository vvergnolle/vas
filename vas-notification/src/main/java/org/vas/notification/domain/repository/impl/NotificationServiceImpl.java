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
