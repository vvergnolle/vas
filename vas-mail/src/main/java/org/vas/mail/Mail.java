package org.vas.mail;

public class Mail {

  public final String to;
  public final String from;
  public final String body;
  public final String subject;
  public final boolean scriptedBody;

  public Mail(String to, String from, String body, String subject) {
    this(to, from, body, subject, true);
  }

  public Mail(String to, String from, String body, String subject, boolean scriptedBody) {
    super();
    this.to = to;
    this.from = from;
    this.body = body;
    this.subject = subject;
    this.scriptedBody = scriptedBody;
  }

}
