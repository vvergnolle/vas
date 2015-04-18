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
package org.vas.http.resource;

public final class HttpResponse {

  static final byte[] EMPTY_BYTES = new byte[0];
  public static final HttpResponse EMPTY = new HttpResponse(EMPTY_BYTES);

  protected byte[] bytes;
  /*
   * Identify the response with a tag - useful when responses are joined and the
   * callback has to recognize each of them.
   */
  protected String marker = "";

  public HttpResponse(byte[] bytes) {
    super();
    this.bytes = bytes;
  }

  public byte[] bytes() {
    return bytes;
  }

  public void clear() {
    bytes = null;
  }

  public String marker() {
    return marker;
  }

  public void marker(String marker) {
    this.marker = marker;
  }
}
