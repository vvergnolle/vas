/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
    Response<UserBean> response = vasRest.withBasic(username, username).get("/rest/users/infos", UserBean.class);

    assertThat(response.status).as("The server should respond with UNAUTHORIZED").isEqualTo(401);
  }

  @Test
  public void itShouldAuthenticateTestUser() {
    String username = "test";
    Response<UserBean> response = vasRest.withBasic(username, username).get("/rest/users/infos", UserBean.class);

    assertThat(response.status).as("The server should response with OK").isEqualTo(200);
    assertThat(response.content.getUsername()).as("The user username should be equals to 'test'").isEqualTo(username);
  }
}
