package org.vas.commons.bean;

/**
 * Pojo that will be used in Json response to give back a state.
 */
public final class StateBean {

	public final String state;
	public final String msg;
	public final long time;

	public StateBean(String state, String msg, long time) {
	  super();
	  this.state = state;
	  this.msg = msg;
	  this.time = time;
  }
	
	public static StateBean success() {
		return new StateBean("success", "Success", System.currentTimeMillis());
	}
	
	public static StateBean success(String msg) {
		return new StateBean("success", msg, System.currentTimeMillis());
	}
	
	public static StateBean error(String cause) {
		return new StateBean("error", cause, System.currentTimeMillis());
	}
}