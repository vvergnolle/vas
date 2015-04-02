package org.vas.domain.repository;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.vas.domain.repository.impl.UserRepositoryImpl;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users", daoClass=UserRepositoryImpl.class)
public class User {
	
	/**
	 * Convenient method for password hashing
	 */
	public static String hashPassword(String password) {
		return DigestUtils.sha512Hex(password);
	}

	/**
	 * Same as {@link User#hashPassword(String)} but with a char array
	 * 
	 * @param password
	 * @return
	 */
	public static String hashPassword(char[] password) {
		return DigestUtils.sha512Hex(new String(password));
	}
	
	/**
	 * Database field names
	 */
	
	public static final String ID = "id";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	/**
	 * Roles
	 */
	
	public static final String ANONYMOUS = "Anonymous";
	public static final String ROLE = "User";
	public static final Set<String> ROLES = new HashSet<>(1);
	static {
		ROLES.add(ROLE);
	}

	@DatabaseField(generatedId = true)
	public transient int id;

	@DatabaseField
	public String username;

	@DatabaseField
	public String password;
	
	/**
	 * Set transient in order to prevent jackson serializer to return it in HTTP responses
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
