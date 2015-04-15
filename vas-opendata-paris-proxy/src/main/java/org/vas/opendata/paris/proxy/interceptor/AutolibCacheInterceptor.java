package org.vas.opendata.paris.proxy.interceptor;

import org.vas.commons.station.Autolib;
import org.vas.commons.station.Stations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class AutolibCacheInterceptor extends InMemoryCacheInterceptor<Autolib> {

  @Override
  protected Autolib toStation(JsonElement record, Stations<Autolib> stations) {
    JsonObject object = record.getAsJsonObject();
    Autolib autolib = new Autolib();

    JsonArray coordinates = object.getAsJsonObject("geometry").getAsJsonArray("coordinates");
    autolib.setLat(coordinates.get(0).getAsFloat());
    autolib.setLng(coordinates.get(1).getAsFloat());

    JsonObject fields = object.getAsJsonObject("fields");
    autolib.setId(fields.getAsJsonPrimitive("identifiant_autolib").getAsString());
    autolib.setTiers(fields.getAsJsonPrimitive("tiers").getAsInt());
    autolib.setAbri(fields.getAsJsonPrimitive("abri").getAsInt());
    autolib.setAutolib(fields.getAsJsonPrimitive("autolib").getAsInt());

    return autolib;
  }
}
