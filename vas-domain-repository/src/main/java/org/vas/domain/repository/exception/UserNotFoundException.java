package org.vas.domain.repository.exception;

public class UserNotFoundException extends DomainRepositoryException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String username) {
		super("User " + username + " not found");
	}
}
