package org.vas.http.resource;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HttpEndpoint {

  protected String baseUrl;
  protected Map<Method, HttpResource> resources = new HashMap<>();

  public HttpEndpoint(String baseUrl) {
    super();
    this.baseUrl = baseUrl;
  }

  public HttpResource lookupResource(Method method) {
    return resources.get(method);
  }

  void addResource(Method method, HttpResource resource) {
    resources.put(method, resource);
  }
}
