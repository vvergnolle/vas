package org.vas.http.resource;

import java.nio.ByteBuffer;

public class HttpConverterRepository {

  @SuppressWarnings("unchecked")
  public <T> HttpResponseConverter<T> lookup(HttpResource resource, Class<?> returnType) {
    if(returnType == HttpResponse.class) {
      return (in) -> (T) in;
    } else if(returnType == byte[].class) {
      return (in) -> (T) in.bytes;
    } else if(returnType == String.class) {
      return (in) -> (T) new String(in.bytes);
    } else if(returnType == ByteBuffer.class) {
      return (in) -> (T) ByteBuffer.wrap(in.bytes);
    }

    throw new UnsupportedOperationException(
      "Support only raw types, [byte[], ByteBuffer, String, HttpResponse] are allowed");
  }
}
