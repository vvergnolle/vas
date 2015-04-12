package org.vas.notification.exception;

public class NotificationNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NotificationNotFoundException() {
    super();
  }

  public NotificationNotFoundException(String message, Throwable cause, boolean enableSuppression,
    boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public NotificationNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotificationNotFoundException(String message) {
    super(message);
  }

  public NotificationNotFoundException(Throwable cause) {
    super(cause);
  }
}
