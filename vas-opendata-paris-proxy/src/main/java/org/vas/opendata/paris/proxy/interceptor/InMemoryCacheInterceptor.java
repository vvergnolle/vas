package org.vas.opendata.paris.proxy.interceptor;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.vas.commons.station.LatLng;
import org.vas.commons.station.Stations;
import org.vas.commons.utils.GsonUtils;
import org.vas.http.resource.HttpResourceInterceptor;
import org.vas.http.resource.HttpResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class InMemoryCacheInterceptor<T extends LatLng> implements HttpResourceInterceptor {

  protected final Map<String, Reference<byte[]>> cache = new HashMap<>();
  protected final JsonParser jsonParser = new JsonParser();

  @Override
  public HttpResponse intercept(Method method, String url, Supplier<HttpResponse> value) {
    String key = key(url);
    byte[] bytes = null;
    Reference<byte[]> ref = cache.get(key);

    if(ref == null) {
      bytes = loadAndFilter(value, key);
    } else {
      bytes = ref.get();

      if(bytes == null) {
        bytes = loadAndFilter(value, key);
      }
    }

    return new HttpResponse(bytes);
  }

  private byte[] loadAndFilter(Supplier<HttpResponse> value, String key) {
    byte[] bytes;
    Reference<byte[]> ref;
    HttpResponse response = value.get();
    byte[] tmpBytes = response.bytes();

    JsonElement json = jsonParser.parse(new String(tmpBytes));
    Stations<T> stations = newStations();
    bind(json, stations);

    bytes = toBytes(stations);
    ref = newRef(bytes);
    cache.put(key, ref);
    return bytes;
  }

  protected Reference<byte[]> newRef(byte[] bytes) {
    return new WeakReference<>(bytes);
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

  protected Stations<T> newStations() {
    return new Stations<T>();
  }
}
