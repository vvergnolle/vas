package org.vas.jaxrs;

import javax.ws.rs.core.Response;

import org.vas.commons.bean.DomainBean;

public final class Responses {

	private Responses() {}

	public static Response notImplemented() {
		return Response.status(501).build();
	}
	
	public static Response noContent() {
		return Response.noContent().build();
	}

	public static Response created(int id) {
		return Response.status(201).entity(DomainBean.of(id)).build();
	}

	public static Response ok() {
		return Response.status(200).build();
	}

	public static Response ok(Object obj) {
		return Response.status(200).entity(obj).build();
	}

	public static Response status(int status) {
		return Response.status(status).build();
	}

	public static Response status(int status, Object obj) {
		return Response.status(status).entity(obj).build();
	}
}
