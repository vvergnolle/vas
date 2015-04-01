package org.vas.http.resource;

import java.util.function.Function;

@FunctionalInterface
public interface HttpResponseConverter<T> extends Function<HttpResponse, T> {
}