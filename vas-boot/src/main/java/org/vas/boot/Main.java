package org.vas.boot;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ServiceLoader;

import org.apache.commons.io.IOUtils;
import org.vas.commons.context.BootContext;
import org.vas.commons.http.HttpHandlerDescriptor;
import org.vas.commons.http.HttpHandlerPostProcessor;
import org.vas.domain.repository.Database;
import org.vas.domain.repository.Repositories;
import org.vas.domain.repository.fixture.UserFixtureLoader;
import org.vas.inject.ServiceContainer;
import org.vas.inject.ServiceContainers;

/**
 * VAS Main class.
 * 
 * <br/><br/>
 * 
 * Responsible to boot the application.
 */
public class Main {
	
	
	/**
	 * Server constants
	 */
	
	static final int IO_THREADS = 4;
	static final int SERVER_PORT = 8080;
	static final String SERVER_HOST = "localhost";

	static final int SERVER_BUFFER_SIZE = 8096;
	static final String CONTEXT_NAME = "/vas";
	
	/**
	 * Application properties
	 * 
	 */
	
	static final String CONF_PROPERTY = "vas.conf";
	static final String FIXTURES_USER_PROPERTY = "vas.fixtures.user";
	static final String WAR_NAME = "vas.war";
	
	/**
	 * Exit codes
	 * 
	 */
	
	static final int ERR_FAIL_TO_LOCATE_APP_CONFIG_FILE = -1;
	static final int ERR_FAIL_TO_READ_USER_FIXTURE_FILE = -2;

	/**
	 * Files names
	 */

	static final String USER_FIXTURE_FILE = "user.fixture.yaml";

	
	public static void main(String[] args) throws Exception {
		PathHandler handler = Handlers.path();
		lookupHandlerDescriptors(handler);
		
		Properties properties = loadProperties();
		
		ServiceContainer serviceContainer = ServiceContainers.defaultContainer();
		serviceContainer.init(properties);
		
		// Make sure tables are ok
		serviceContainer.get(Database.class).createTables();
		
		// Load some users
		loadFixtures(serviceContainer);
		
		ServletContainer servletContainer = Servlets.defaultContainer();
		DeploymentInfo deploymentInfo = Servlets
			.deployment()
			.setDeploymentName(WAR_NAME)
			.setClassLoader(classLoader())
			.setContextPath(CONTEXT_NAME);
	
		BootContext context = new BootContext() {
			
			@Override
			public Properties properties() {
			  return properties;
			}
			
			@Override
			public DeploymentInfo deploymentInfo() {
			  return deploymentInfo;
			}
			
			@Override
			public PathHandler pathHandler() {
				return handler;
			}
			
			@Override
			public void inject(Object object) {
				serviceContainer.inject(object);
			}
			
			@Override
			public <T> T getService(Class<T> klass) {
				return serviceContainer.get(klass);
			}
		};
		
		lookupHandlerPostProcessors(context);
		
		DeploymentManager deploymentManager = servletContainer.addDeployment(deploymentInfo);
		deploymentManager.deploy();
		
		// The default deployment will be available at CONTEXT_NAME
		handler.addPrefixPath(CONTEXT_NAME, deploymentManager.start());

		boot(handler);
	}

	static void loadFixtures(ServiceContainer serviceContainer) throws Exception {
		String userFixtureFile = System.getProperty(FIXTURES_USER_PROPERTY);
		
	  InputStream fis = null;
		try {
			fis = new FileInputStream(userFixtureFile);
		}
		catch(FileNotFoundException | NullPointerException e) {
			fis = classLoader().getResourceAsStream(USER_FIXTURE_FILE);
		}
		catch (SecurityException e) {
			System.err.println("Vas doesn't have the right to read the '" + userFixtureFile + "' file - try to fix the security policy");
			e.printStackTrace(System.err);
			System.exit(ERR_FAIL_TO_READ_USER_FIXTURE_FILE);
		}
		finally {
			if(fis != null) {
				new UserFixtureLoader().load(fis, serviceContainer.get(Repositories.class));
				IOUtils.closeQuietly(fis);
			}
		}
  }

	static ClassLoader classLoader() {
	  return Main.class.getClassLoader();
  }

	/**
	 * Load post processors
	 */
	static void lookupHandlerPostProcessors(BootContext context) {
		ServiceLoader
			.load(HttpHandlerPostProcessor.class)
			.iterator()
			.forEachRemaining(
				processor -> processor.postProcess(context)
			);
  }

	/**
	 * Load http handler descriptors
	 */
	static void lookupHandlerDescriptors(PathHandler handler) {
		ServiceLoader
			.load(HttpHandlerDescriptor.class)
			.iterator()
			.forEachRemaining(descriptor -> 
					handler.addPrefixPath(descriptor.uri(), instanciate(descriptor.httpHandler()))
			);
  }

	/**
	 * Create & start the server
	 */
	static void boot(PathHandler handler) {
		Undertow server = Undertow
			.builder()
			.setIoThreads(IO_THREADS)
			.setBufferSize(SERVER_BUFFER_SIZE)
			.setWorkerThreads(6)
			.addHttpListener(SERVER_PORT, SERVER_HOST)
			.setHandler(handler)
			.build();
		
		server.start();
  }

	/**
	 * Load vas configuration file
	 */
	static Properties loadProperties() throws IOException {
		String propertiesLocation = System.getProperty(CONF_PROPERTY);
		if(propertiesLocation == null || propertiesLocation.isEmpty()) {
			System.err.println("The application configuration file must be indicated");
			System.exit(ERR_FAIL_TO_LOCATE_APP_CONFIG_FILE);
		}
		
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesLocation));
	  return properties;
  }

	@SuppressWarnings("unchecked")
	static <T> T instanciate(Class<?> klass) {
		try {
			return (T) klass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
