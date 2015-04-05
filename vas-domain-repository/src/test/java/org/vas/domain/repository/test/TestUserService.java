package org.vas.domain.repository.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.testng.annotations.Test;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserService;
import org.vas.test.AbstractVasRuntimeTest;

public class TestUserService extends AbstractVasRuntimeTest {

	@Inject
	UserService userService;

	@Test
	public void itShouldHaveService() {
		assertThat(userService).as("The user service shouldn't be null").isNotNull();
	}

	@Test
	public void itShouldFetchUser() {
		User user = userService.fetch(1);
		assertThat(user.username).as("The user username should be 'test'").isEqualTo("test");
	}
}
