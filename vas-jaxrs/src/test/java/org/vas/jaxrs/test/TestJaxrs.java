package org.vas.jaxrs.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;
import org.vas.jaxrs.test.resource.FooBarResource;
import org.vas.test.AbstractVasRestTest;
import org.vas.test.rest.Response;

public class TestJaxrs extends AbstractVasRestTest {

	@Test
	public void itShould() {
		Response<String> response = vasRest
			.get("/rest/test/jaxrs/foobar", String.class);

		assertThat(response.content)
			.as("The content should be equals to '" + FooBarResource.STRING + "'")
			.isEqualTo(FooBarResource.STRING);
	}
}
