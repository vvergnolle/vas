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

  public static <T> Response<T> of(int status, T content, byte[] raw) {
    return new Response<T>(status, content, raw);
  }

  public static <T> Response<T> of(int status, T content, byte[] raw, MsgBean msg) {
    return new Response<T>(status, content, raw, msg);
  }
}
