package org.vas.http.resource;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public interface HttpResourceInterceptor {

  HttpResponse intercept(Method method, String url, Supplier<HttpResponse> value);
}
