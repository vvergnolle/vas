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
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class AddressModule extends AbstractModule implements ModuleDescriptor {

  private Properties properties;

  @Override
  protected void configure() {
    try {
      String jdbcUrl = properties.getProperty("vas.db.url");

      ComboPooledDataSource ds = new ComboPooledDataSource();
      ds.setUser(properties.getProperty("vas.db.user", "vas"));
      ds.setPassword(properties.getProperty("vas.db.pwd", ""));
      ds.setJdbcUrl(jdbcUrl);

      String rawPolicy = properties.getProperty("vas.db.policy", "normal").toUpperCase();
      DatabasePolicy policy = DefaultDatabasePolicy.NORMAL;
      try {
        policy = DefaultDatabasePolicy.valueOf(rawPolicy);
      } finally {
        policy.configure(ds);
      }

      ConnectionSource connectionSource = new DataSourceConnectionSource(ds, jdbcUrl);
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

  interface DatabasePolicy {

    void configure(ComboPooledDataSource ds);
  }

  enum DefaultDatabasePolicy implements DatabasePolicy {
    SMALL(2, 2, 10), NORMAL(5, 3, 15), HIGH(10, 4, 40), HUGE(15, 3, 65);

    int poolSize;
    int maxStmts;
    int aquireInc;
    int retry = 3;
    int retryDelay = 60;
    int maxIdle = 60 * 60 * 2;
    int maxAge = 60 * 60 * 24 * 2;

    private DefaultDatabasePolicy(int poolSize, int aquireInc, int maxStmts) {
      this.poolSize = poolSize;
      this.aquireInc = aquireInc;
      this.maxStmts = maxStmts;
    }

    @Override
    public void configure(ComboPooledDataSource ds) {
      ds.setMinPoolSize(poolSize);
      ds.setInitialPoolSize(poolSize);
      ds.setMaxPoolSize(poolSize * 3);
      ds.setAcquireIncrement(aquireInc);
      ds.setMaxIdleTime(maxIdle);
      ds.setMaxConnectionAge(maxAge);
      ds.setMaxStatementsPerConnection(maxStmts);
      ds.setAcquireRetryAttempts(retry);
      ds.setAcquireRetryDelay(retryDelay);
    }
  }
}
