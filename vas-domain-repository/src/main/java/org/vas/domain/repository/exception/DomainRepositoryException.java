package org.vas.domain.repository.exception;

public class DomainRepositoryException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DomainRepositoryException() {
    super();
  }

  public DomainRepositoryException(String message, Throwable cause, boolean enableSuppression,
    boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public DomainRepositoryException(String message, Throwable cause) {
    super(message, cause);
  }

  public DomainRepositoryException(String message) {
    super(message);
  }

  public DomainRepositoryException(Throwable cause) {
    super(cause);
  }
}
