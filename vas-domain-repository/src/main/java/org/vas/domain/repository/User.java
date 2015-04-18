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
package org.vas.domain.repository;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.vas.domain.repository.impl.UserRepositoryImpl;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users", daoClass = UserRepositoryImpl.class)
public class User {

  public static final String ID = "id";
  public static final String EMAIL = "email";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String CREATED_AT = "createdAt";

  /**
   * Roles
   */

  public static final String ROLE = "User";
  public static final Set<String> ROLES = new HashSet<>(1);
  static {
    ROLES.add(ROLE);
  }

  /**
   * Convenient method for password hashing
   */
  public static String hashPassword(String password) {
    return Hashing.sha512().hashString(password, Charsets.UTF_8).toString();
  }

  /**
   * Same as {@link User#hashPassword(String)} but with a char array
   * 
   * @param password
   * @return
   */
  public static String hashPassword(char[] password) {
    return Hashing.sha512().hashString(new String(password), Charsets.UTF_8).toString();
  }

  /**
   * Database field names
   */

  @DatabaseField(generatedId = true)
  public int id;

  @DatabaseField
  public String username;

  @DatabaseField
  public String email;

  @DatabaseField
  public String password;

  @DatabaseField
  public Date createdAt;

  @ForeignCollectionField
  public transient ForeignCollection<Address> addresses;

  public void hash() {
    password = hashPassword(password);
  }

  public String toString() {
    return username;
  }
}
