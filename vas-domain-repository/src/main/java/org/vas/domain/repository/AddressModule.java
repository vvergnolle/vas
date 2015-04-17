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
package org.vas.domain.repository;

import static org.vas.commons.utils.FunctionalUtils.quiet;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

import org.vas.commons.utils.OrderedComparator;
import org.vas.domain.repository.exception.DomainRepositoryException;
import org.vas.domain.repository.impl.AddressServiceImpl;
import org.vas.domain.repository.impl.RepositoriesImpl;
import org.vas.domain.repository.impl.UserServiceImpl;
import org.vas.domain.repository.listener.DomainRepositoryEventListener;
import org.vas.inject.ModuleDescriptor;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class AddressModule extends AbstractModule implements ModuleDescriptor {

  private Properties properties;

  @Override
  protected void configure() {
    try {
      ConnectionSource connectionSource = new JdbcConnectionSource(properties.getProperty("vas.db.url"),
        properties.getProperty("vas.db.user", "vas"), properties.getProperty("vas.db.pwd", ""));

      bind(ConnectionSource.class).toInstance(connectionSource);

      bind(Database.class).toInstance(() -> {
        TableUtils.createTable(connectionSource, User.class);
        TableUtils.createTable(connectionSource, Address.class);

        createTableByDescriptors(connectionSource);
      });

      bind(AddressRepository.class).toInstance(
        (AddressRepository) DaoManager.createDao(connectionSource, Address.class));

      bind(UserRepository.class).toInstance((UserRepository) DaoManager.createDao(connectionSource, User.class));

      bind(Repositories.class).to(RepositoriesImpl.class);
      bind(AddressService.class).to(AddressServiceImpl.class);
      bind(UserService.class).to(UserServiceImpl.class);
    } catch (SQLException e) {
      throw new DomainRepositoryException(e);
    }

    bind(DomainRepositoryEventListener.class).asEagerSingleton();
  }

  private void createTableByDescriptors(ConnectionSource connectionSource) {
    List<CreateTableDescriptor> descriptors = Lists.newArrayList(ServiceLoader.load(CreateTableDescriptor.class)
      .iterator());
    descriptors.sort(new OrderedComparator());
    descriptors.forEach(d -> quiet(() -> TableUtils.createTable(connectionSource, d.domain())));
  }

  @Override
  public Module module(Properties properties) {
    this.properties = properties;
    return this;
  }
}
