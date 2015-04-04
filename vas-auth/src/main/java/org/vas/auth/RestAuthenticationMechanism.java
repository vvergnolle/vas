package org.vas.auth;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMechanism.AuthenticationMechanismOutcome;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.domain.repository.UserRepository;

/**
 * Custom {@link AuthenticationMechanism} based on the HTTP Basic system for users authentications.
 * 
 */
public class RestAuthenticationMechanism implements AuthenticationMechanism {
	
	protected static final String MECHANISM_NAME = "REST-AUTH-MECHANISM";

	private static final String COLON = ":";
	private static final String BASIC = "Basic ";
	private static final ChallengeResult CHALLENGE_RESULT = new ChallengeResult(true, StatusCodes.UNAUTHORIZED);

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected final UserRepository repository;

	public RestAuthenticationMechanism(UserRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Override
	public ChallengeResult sendChallenge(HttpServerExchange exchange, SecurityContext securityContext) {
		return CHALLENGE_RESULT;
	}

	@Override
	public AuthenticationMechanismOutcome authenticate(HttpServerExchange exchange, SecurityContext securityContext) {
		Account account = securityContext.getAuthenticatedAccount();
		if(account != null) {
			if(logger.isDebugEnabled()) {
				logger.debug("User {} already logged in - nothing to do", account.getPrincipal().getName());
			}

			return AuthenticationMechanismOutcome.AUTHENTICATED;
		}
		 
		try {
			HeaderValues header = authorizationHeader(exchange);
			if(header == null) {
				return AuthenticationMechanismOutcome.NOT_AUTHENTICATED;
			}
			
			String authorization = header.getFirst();
			byte[] bytes = authorizationBytes(securityContext, authorization);
			String[] credentials = authorizationCredentials(securityContext, bytes);

			account = verify(
				credentials,
				securityContext.getIdentityManager()
			).orElseThrow(() ->
				new AuthenticationException(AuthenticationMechanismOutcome.NOT_AUTHENTICATED, "Authentication failed to log the user")
			);
		} catch (AuthenticationException e) {
			securityContext.authenticationFailed(e.getMessage(), MECHANISM_NAME);
			return e.outcome;
		}

		securityContext.authenticationComplete(account, MECHANISM_NAME, true);
		return AuthenticationMechanismOutcome.AUTHENTICATED;
	}

	protected Optional<Account> verify(String[] credentials, IdentityManager identityManager) {
		try {
			Account account = identityManager.verify(credentials[0], new PasswordCredential(credentials[1].toCharArray()));
			return Optional.ofNullable(account);
		}
		catch(NullPointerException e) {
			throw new AuthenticationException(AuthenticationMechanismOutcome.NOT_AUTHENTICATED, "Malformated credentials - " + e.getMessage());
		}
  }

	protected String[] authorizationCredentials(SecurityContext securityContext, byte[] bytes) {
		String[] credentials = new String(bytes).split(COLON);
		if (credentials == null || credentials.length < 2) {
			throw new AuthenticationException(
				AuthenticationMechanismOutcome.NOT_AUTHENTICATED, "Authorization header must be splitted in two part separated by semicolon");
		}

		return credentials;
	}

	protected HeaderValues authorizationHeader(HttpServerExchange exchange) {
		HeaderValues header = exchange.getRequestHeaders().get(Headers.AUTHORIZATION);
		if(logger.isDebugEnabled()) {
			logger.debug("Authorization header: " + header);
		}
		
		return header;
	}

	protected byte[] authorizationBytes(SecurityContext securityContext, String authorization) {
		int idx = authorization.indexOf(BASIC);
		if (idx < 0) {
			throw new AuthenticationException(AuthenticationMechanismOutcome.NOT_AUTHENTICATED,
			    "Malformated Authorization header: " + authorization);
		}

		try {
			String encodedCredentials = authorization.substring(idx + BASIC.length());
			if(logger.isTraceEnabled()) {
				logger.trace("Encoded credentials: {}", encodedCredentials);
			}
			
			return Base64.getDecoder().decode(encodedCredentials);
		} catch (IllegalArgumentException e) {
			logError(e);

			throw new AuthenticationException(
				AuthenticationMechanismOutcome.NOT_AUTHENTICATED,
				"Error when trying to decode the base64 digest for the Authorization header - does the authorization is really encoded with base64 scheme ?",
				e
			);
		} catch (IndexOutOfBoundsException e) {
			logError(e);
			throw new AuthenticationException(AuthenticationMechanismOutcome.NOT_AUTHENTICATED);
		}
	}

	protected AuthenticationException notAuthenticatedException() {
		return new AuthenticationException(AuthenticationMechanismOutcome.NOT_AUTHENTICATED);
	}

	protected void logError(Exception e) {
	  if (logger.isErrorEnabled()) {
	  	logger.error("Error occured", e);
	  }
  }
}

/**
 * Exception that indicate an exception in the authenticate mechanism.
 */
class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	final AuthenticationMechanismOutcome outcome;

	public AuthenticationException(AuthenticationMechanismOutcome outcome, String message) {
		super(message);
		this.outcome = outcome;
	}

	public AuthenticationException(AuthenticationMechanismOutcome outcome) {
		super();
		this.outcome = outcome;
	}

	public AuthenticationException(AuthenticationMechanismOutcome outcome, String message, Throwable cause) {
		super(message, cause);
		this.outcome = outcome;
	}
}