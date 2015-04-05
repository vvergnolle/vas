package org.vas.domain.repository.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.testng.annotations.Test;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;
import org.vas.test.AbstractVasRuntimeTest;

public class TestUserRepository extends AbstractVasRuntimeTest {

	@Inject
	UserRepository userRepository;
	
	@Test
	public void itShouldHaveRepository() {
		assertThat(userRepository).as("The user repository shouldn't be null").isNotNull();
	}
	
	@Test
	public void itShouldAuthenticateUser() {
		String username = "test";
		User user = userRepository.authenticate(username, username.toCharArray());
		assertThat(user.username).as("The authenticated user username should be equals to '"+ username +"'").isEqualTo(username);
	}
}