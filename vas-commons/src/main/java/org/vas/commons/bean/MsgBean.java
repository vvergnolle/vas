package org.vas.commons.bean;

public class MsgBean {

	public final String msg;
	public final long time = System.currentTimeMillis();

	public MsgBean(String msg) {
	  super();
	  this.msg = msg;
	}
	
	public static MsgBean of(String msg) {
		return new MsgBean(msg);
	}
}