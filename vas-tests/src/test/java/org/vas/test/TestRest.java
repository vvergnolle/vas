package org.vas.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;
import org.vas.test.rest.Response;

public class TestRest extends AbstractVasRestTest {

  @Test
  public void itShouldQueryGoogle() {
    Response<String> response = rest.get("https://google.fr", String.class);
    assertThat(response.content).as("The google content shouldn't be empty").isNotEmpty();
  }
}
