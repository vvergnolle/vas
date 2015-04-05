package org.vas.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.testng.annotations.Test;
import org.vas.launcher.Env;
import org.vas.launcher.ServerConf;
import org.vas.launcher.Vas;

public class VasBootListenerTest extends AbstractVasServerTest {

	@Inject
	Vas vas;

	@Inject
	Env env;

	@Inject
	ServerConf conf;

	@Test
	public void itShouldHaveVas() {
		assertThat(vas).as("Vas shouldn't be null").isNotNull();
	}

	@Test
	public void itShouldHaveEnv() {
		assertThat(env).as("Env shouldn't be null").isNotNull();
	}

	@Test
	public void itShouldHaveServerConf() {
		assertThat(conf).as("ServerConf shouldn't be null").isNotNull();
	}
}
