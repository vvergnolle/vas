package org.vas.mail;

import org.vas.commons.event.VasEvent;

public class SendMailEvent extends VasEvent {

  public final Mail mail;

  public SendMailEvent(Object sender, Mail mail) {
    super(sender);
    this.mail = mail;
  }
}
