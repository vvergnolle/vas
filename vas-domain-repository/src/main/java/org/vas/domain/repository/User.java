package org.vas.domain.repository;

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
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";

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
  public String password;

  /**
   * Set transient in order to prevent jackson serializer to return it in HTTP
   * responses
   */
  @ForeignCollectionField
  public transient ForeignCollection<Address> addresses;

  public void hash() {
    password = hashPassword(password);
  }

  public String toString() {
    return username;
  }
}
