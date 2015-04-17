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
package org.vas.commons.bean;

import java.security.Principal;

import org.vas.commons.security.UserPrincipal;

/**
 * Pojo that will give back informations on a user
 */
public final class UserBean {

  private int id;
  private String username;
  private long time = System.currentTimeMillis();

  public UserBean() {
    super();
  }

  UserBean(int id, String username) {
    super();
    this.id = id;
    this.username = username;
  }

  UserBean(int id, Principal principal) {
    this(id, principal.getName());
  }

  UserBean(UserPrincipal principal) {
    this(principal.id, principal.user);
  }

  public static UserBean of(UserPrincipal principal) {
    return new UserBean(principal);
  }

  public static UserBean of(int id, Principal principal) {
    return new UserBean(id, principal);
  }

  public static UserBean of(int id, String username) {
    return new UserBean(id, username);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }
}
