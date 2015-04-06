package org.vas.domain.repository.listener;

import static org.vas.commons.utils.FunctionalUtils.quiet;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.event.DefaultDeploymentEvent;
import org.vas.commons.io.FileLoader;
import org.vas.domain.repository.Database;
import org.vas.domain.repository.UserRepository;
import org.vas.domain.repository.fixture.UserFixtureLoader;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class DomainRepositoryEventListener {

  private static final int ERR_FAIL_TO_READ_USER_FIXTURE_FILE = -2;
  private static final String USER_FIXTURE_FILE = "user.fixture.yaml";
  private static final String FIXTURES_USER_PROPERTY = "vas.fixtures.user";

  protected static ClassLoader classLoader() {
    return DomainRepositoryEventListener.class.getClassLoader();
  }

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected Database database;
  protected UserRepository userRepository;

  @Inject
  public DomainRepositoryEventListener(Database database, UserRepository userRepository, EventBus eventBus) {
    super();
    this.database = database;
    this.userRepository = userRepository;

    eventBus.register(this);
  }

  @Subscribe
  public void onDefaultDeployment(DefaultDeploymentEvent event) throws Exception {
    database.createTables();
    quiet(this::loadUserFixture);
  }

  protected void loadUserFixture() throws Exception {
    String userFixtureFile = System.getProperty(FIXTURES_USER_PROPERTY, USER_FIXTURE_FILE);

    InputStream fis = null;
    try {
      fis = new FileLoader(userFixtureFile).load();
    } catch (FileNotFoundException e) {
      if(logger.isWarnEnabled()) {
        logger.warn("User fixture file not found", e);
      }
    } catch (SecurityException e) {
      System.err.println("Vas doesn't have the right to read the '" + userFixtureFile
        + "' file - try to fix the security policy");
      e.printStackTrace(System.err);
      System.exit(ERR_FAIL_TO_READ_USER_FIXTURE_FILE);
    }

    if(fis != null) {
      new UserFixtureLoader().load(fis, userRepository);
      IOUtils.closeQuietly(fis);
    }
  }
}
