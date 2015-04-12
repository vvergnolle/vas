package org.vas.mail;

public interface MailDispatcher extends Runnable {

  void dispatch(Mail mail);
}
