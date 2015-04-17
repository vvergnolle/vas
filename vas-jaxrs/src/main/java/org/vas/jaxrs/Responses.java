/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
