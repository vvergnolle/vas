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
package org.vas.opendata.paris.proxy.interceptor;

import java.lang.reflect.Method;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.vas.commons.station.LatLng;
import org.vas.commons.station.Stations;
import org.vas.commons.utils.GsonUtils;
import org.vas.http.resource.HttpResourceInterceptor;
import org.vas.http.resource.HttpResponse;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class InMemoryCacheInterceptor<T extends LatLng> implements HttpResourceInterceptor {

  protected final Cache<String, byte[]> cache = CacheBuilder.newBuilder().softValues().initialCapacity(50)
    .expireAfterWrite(1, TimeUnit.HOURS).build();
  protected final JsonParser jsonParser = new JsonParser();

  @Override
  public HttpResponse intercept(Method method, String url, Supplier<HttpResponse> value) {
    String key = key(url);
    byte[] bytes = cache.getIfPresent(key);

    if(bytes == null) {
      bytes = loadAndFilter(value, key);
    }

    return new HttpResponse(bytes);
  }

  private byte[] loadAndFilter(Supplier<HttpResponse> value, String key) {
    HttpResponse response = value.get();
    JsonElement json = jsonParser.parse(new String(response.bytes()));
    Stations<T> stations = new Stations<>();
    bind(json, stations);

    byte[] bytes = toBytes(stations);
    cache.put(key, bytes);
    return bytes;
  }

  protected void bind(JsonElement json, Stations<T> stations) {
    JsonObject object = json.getAsJsonObject();

    int hits = object.getAsJsonPrimitive("nhits").getAsInt();
    stations.setHits(hits);

    int rows = object.getAsJsonObject("parameters").getAsJsonPrimitive("rows").getAsInt();
    stations.setRows(rows);

    object.getAsJsonArray("records").forEach(r -> stations.addStation(toStation(r, stations)));
  }

  protected abstract T toStation(JsonElement r, Stations<T> stations);

  protected byte[] toBytes(Stations<? extends LatLng> stations) {
    return GsonUtils.GSON.toJson(stations).getBytes();
  }

  protected String key(String url) {
    return Base64.getEncoder().encodeToString(url.getBytes());
  }
}
