package org.vas.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.vas.launcher.Env;
import org.vas.test.annotation.VasBoot;
import org.vas.test.listener.VasServerTestListener;

/**
 * Test based only on the Vas runtime - no HTTP features
 *
 */
@VasBoot(custom = true, profile = Env.Profile.RUNTIME)
@Listeners(VasServerTestListener.class)
public class AbstractVasRuntimeTest {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
}
