package org.vas.http.resource;

public final class HttpResponse {

  protected byte[] bytes;

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
}
