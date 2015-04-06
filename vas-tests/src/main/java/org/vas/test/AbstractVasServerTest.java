package org.vas.test;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.vas.launcher.Env;
import org.vas.launcher.ServerConf;
import org.vas.test.annotation.VasBoot;
import org.vas.test.listener.VasServerTestListener;

@VasBoot
@Listeners(VasServerTestListener.class)
public abstract class AbstractVasServerTest {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  protected ServerConf conf;

  @Inject
  protected Env env;
}
