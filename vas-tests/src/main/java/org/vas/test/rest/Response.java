package org.vas.test.rest;

import org.vas.commons.bean.MsgBean;

public class Response<T> {

	public final int status;
	public final T content;
	public final byte[] raw;
	public final MsgBean msg;

	public Response(int status, T content, byte[] raw, MsgBean msg) {
	  super();
	  this.raw = raw;
	  this.status = status;
	  this.content = content;
	  this.msg = msg;
  }
	
	public Response(int status, T content, byte[] raw) {
		this(status, content, raw, null);
  }
	
	public static <T> Response<T> of(int status,  T content, byte[] raw) {
		return new Response<T>(status, content, raw);
	}
	
	public static <T> Response<T> of(int status,  T content, byte[] raw, MsgBean msg) {
		return new Response<T>(status, content, raw, msg);
	}
}