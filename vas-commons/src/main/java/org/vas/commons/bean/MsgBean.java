package org.vas.commons.bean;

public class MsgBean {

	private String msg;
	private long time = System.currentTimeMillis();

	public MsgBean() {
		super();
	}

	public MsgBean(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public long getTime() {
		return time;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public static MsgBean of(String msg) {
		return new MsgBean(msg);
	}
}
