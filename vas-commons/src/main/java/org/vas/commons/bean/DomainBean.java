package org.vas.commons.bean;

public class DomainBean {

  private int id;
  private long time = System.currentTimeMillis();

  public DomainBean() {
    super();
  }

  public DomainBean(int id) {
    super();
    this.id = id;
  }

  public static DomainBean of(int id) {
    return new DomainBean(id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }
}
