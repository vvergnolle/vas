package org.vas.auth.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;
import org.vas.commons.bean.UserBean;
import org.vas.test.AbstractVasRestTest;
import org.vas.test.rest.Response;

public class TestRestAuth extends AbstractVasRestTest {

	@Test
	public void itShouldNotAuthenticateUser() {
		String username = "foobar";
		Response<UserBean> response = vasRest
			.withBasic(username, username)
			.get("/rest/users/infos", UserBean.class);

		assertThat(response.status).as("The server should respond with UNAUTHORIZED").isEqualTo(401);
	}
	
	@Test
	public void itShouldAuthenticateTestUser() {
		String username = "test";
		Response<UserBean> response = vasRest
			.withBasic(username, username)
			.get("/rest/users/infos", UserBean.class);

		assertThat(response.status).as("The server should response with OK").isEqualTo(200);
		assertThat(response.content.getUsername()).as("The user username should be equals to 'test'").isEqualTo(username);
	}
}