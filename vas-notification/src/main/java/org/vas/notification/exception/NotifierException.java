package org.vas.notification.exception;

public class NotifierException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NotifierException() {
    super();
  }

  public NotifierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public NotifierException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotifierException(String message) {
    super(message);
  }

  public NotifierException(Throwable cause) {
    super(cause);
  }
}
