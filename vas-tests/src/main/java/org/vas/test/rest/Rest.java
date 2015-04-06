package org.vas.test.rest;

import java.util.Map;

public interface Rest {

  Rest withBasic(String username, String password);

  <T> Response<T> get(String uri, Class<T> klass, Object... args);

  <T> Response<T> post(String uri, Map<String, String> datas, Class<T> klass, Object... args);

  <T> Response<T> post(String uri, Class<T> klass, Object... args);

  <T> Response<T> put(String uri, Map<String, String> datas, Class<T> klass, Object... args);

  <T> Response<T> put(String uri, Class<T> klass, Object... args);

  <T> Response<T> delete(String uri, Class<T> klass, Object... args);
}
