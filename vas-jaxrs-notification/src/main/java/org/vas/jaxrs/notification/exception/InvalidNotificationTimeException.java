package org.vas.jaxrs.notification.exception;

public class InvalidNotificationTimeException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidNotificationTimeException() {
    super();
  }

  public InvalidNotificationTimeException(String message, Throwable cause, boolean enableSuppression,
    boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public InvalidNotificationTimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidNotificationTimeException(String message) {
    super(message);
  }

  public InvalidNotificationTimeException(Throwable cause) {
    super(cause);
  }
}
