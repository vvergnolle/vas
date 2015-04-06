package org.vas.launcher;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;

import java.util.Properties;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.event.DefaultDeploymentEvent;
import org.vas.inject.Services;
import org.vas.inject.ServicesUtil;

import com.google.common.eventbus.EventBus;

class VasImpl implements Vas {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected Undertow server;
  protected EventBus eventBus;
  protected Env env = new Env();
  protected final ServerConf conf;
  protected Properties properties;
  protected BootContextImpl context;
  protected Services services;

  VasImpl() {
    this(new ServerConf());
  }

  VasImpl(ServerConf conf) {
    super();
    this.conf = conf;
  }

  @Override
  public void start() throws Exception {
    // Default path handler
    PathHandler pathHandler = Handlers.path();

    // Handler descriptors
    VasHelper.lookupHandlerDescriptors(pathHandler);
    properties = VasHelper.loadProperties(env);

    // Service container
    services = ServicesUtil.defaultContainer();
    services.init(properties);

    eventBus = services.get(EventBus.class);

    // Notify that we able to create our default deployment
    eventBus.post(new DefaultDeploymentEvent(this));

    if(env.profile == Env.Profile.SERVER) {
      init(pathHandler);

      if(logger.isDebugEnabled()) {
        logger.debug("Vas started (with server)");
      }
    } else {
      if(logger.isDebugEnabled()) {
        logger.debug("Vas started (no server)");
      }
    }
  }

  private void init(PathHandler pathHandler) throws ServletException {
    // Deployment info
    ServletContainer servletContainer = Servlets.defaultContainer();
    DeploymentInfo deploymentInfo = VasHelper.deploymentInfo();

    // Boot context & post processors
    context = new BootContextImpl(properties, pathHandler, deploymentInfo, services);
    VasHelper.lookupHandlerPostProcessors(context);

    // Deployment
    DeploymentManager deploymentManager = servletContainer.addDeployment(deploymentInfo);
    deploymentManager.deploy();

    pathHandler.addPrefixPath(Const.CONTEXT_NAME, deploymentManager.start());
    boot();
  }

  @Override
  public void start(Env env) throws Exception {
    this.env = env;
    start();
  }

  protected void boot() {
    server = Undertow.builder().setIoThreads(conf.ioThreads).setBufferSize(conf.bufferSize).setWorkerThreads(6)
      .addHttpListener(conf.port, conf.host).setHandler(context.rootHttpHandler()).build();
    server.start();
  }

  @Override
  public void restart() throws Exception {
    stop();
    start();
  }

  @Override
  public void restart(Env env) throws Exception {
    this.env = env;
    restart();
  }

  @Override
  public void stop() throws Exception {
    if(server != null) {
      server.stop();

      if(logger.isErrorEnabled()) {
        logger.error("Vas stopped");
      }
    } else {
      if(logger.isErrorEnabled()) {
        logger.error("Vas stopped (no server)");
      }
    }
  }

  @Override
  public Properties properties() {
    return properties;
  }

  @Override
  public Services services() {
    return services;
  }

  @Override
  public ServerConf conf() {
    return conf;
  }

  @Override
  public Env env() {
    return env;
  }
}
