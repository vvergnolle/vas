package org.vas.jaxrs;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;

import org.vas.commons.bean.DomainBean;
import org.vas.commons.bean.StateBean;

public final class Responses {

	private Responses() {}

	public static Response okSuccess() {
		return ok(StateBean.success());
	}
	
	public static Response okSuccess(String msg) {
		return ok(StateBean.success(msg));
	}

	public static Response error(String cause) {
		return status(500, StateBean.error(cause));
	}

	public static Response error(Exception cause) {
		return status(500, StateBean.error(cause.getMessage()));
	}

	public static Response state(List<? super Object> beans) {
		return Response.status(beans == null ? 204 : 200).entity(beans).build();
	}
	
	public static Response notImplemented() {
		return Response.status(501).build();
	}
	
	public static Response noContent() {
		return Response.noContent().build();
	}

	public static Response created(String uri) {
		return Response.created(URI.create(uri)).build();
	}
	
	public static Response created(int id) {
		return Response.status(201).entity(DomainBean.of(id)).build();
	}
	
	public static Response createdSuccess(String uri) {
		return Response.created(URI.create(uri)).entity(StateBean.success()).build();
	}
	
	public static Response createdSuccess(String uri, String msg) {
		return Response.created(URI.create(uri)).entity(StateBean.success(msg)).build();
	}

	public static Response notAcceptable(List<Variant> variants) {
		return Response.notAcceptable(variants).build();
	}

	public static Response notFound() {
		return Response.status(404).build();
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
