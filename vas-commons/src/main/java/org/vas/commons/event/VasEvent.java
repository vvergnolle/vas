package org.vas.commons.event;

public abstract class VasEvent {

  public final Object sender;
  public final long sendAt = System.currentTimeMillis();

  public VasEvent(Object sender) {
    super();
    this.sender = sender;
  }
}
