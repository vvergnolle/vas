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
package org.vas.commons.security;

import io.undertow.security.idm.Account;

import java.security.Principal;
import java.util.Set;

/**
 * Java {@link Principal} that will be used to hold our logged user
 * informations.
 * 
 * Instances of this class will be used in servlets or handlers.
 */
public class UserPrincipal implements Account, Principal {

  public final int id;
  public final String user;
  protected Set<String> roles;

  public UserPrincipal(int id, String user, Set<String> roles) {
    super();
    this.id = id;
    this.user = user;
    this.roles = roles;
  }

  @Override
  public Principal getPrincipal() {
    return this;
  }

  @Override
  public Set<String> getRoles() {
    return roles;
  }

  @Override
  public String getName() {
    return user;
  }
}
