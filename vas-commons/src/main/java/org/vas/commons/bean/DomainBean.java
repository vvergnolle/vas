package org.vas.commons.bean;

public class DomainBean {
	
	public final int id;
	public final long time = System.currentTimeMillis();
	public DomainBean(int id) {
	  super();
	  this.id = id;
  }
	
	
	public static DomainBean of(int id) {
		return new DomainBean(id);
	}
}
