package org.vas.domain.repository;

import java.sql.SQLException;
import java.util.Properties;

import org.vas.domain.repository.exception.DomainRepositoryException;
import org.vas.domain.repository.impl.AddressServiceImpl;
import org.vas.domain.repository.impl.RepositoriesImpl;
import org.vas.domain.repository.impl.UserServiceImpl;
import org.vas.domain.repository.listener.DomainRepositoryEventListener;
import org.vas.inject.ModuleDescriptor;

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

  @Override
  public Module module(Properties properties) {
    this.properties = properties;
    return this;
  }
}
