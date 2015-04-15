package org.vas.opendata.paris.proxy.interceptor;

import org.vas.commons.station.Stations;
import org.vas.commons.station.Velib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class VelibCacheInterceptor extends InMemoryCacheInterceptor<Velib> {

  @Override
  protected Velib toStation(JsonElement record, Stations<Velib> stations) {
    JsonObject object = record.getAsJsonObject();
    Velib velib = new Velib();

    JsonArray coordinates = object.getAsJsonObject("geometry").getAsJsonArray("coordinates");
    velib.setLat(coordinates.get(0).getAsFloat());
    velib.setLng(coordinates.get(1).getAsFloat());

    JsonObject fields = object.getAsJsonObject("fields");
    velib.setId(fields.getAsJsonPrimitive("name").getAsString());
    velib.setDist(fields.getAsJsonPrimitive("dist").getAsInt());
    velib.setAvailable(fields.getAsJsonPrimitive("available_bikes").getAsInt());
    velib.setAvailableStands(fields.getAsJsonPrimitive("available_bike_stands").getAsInt());
    velib.setStands(fields.getAsJsonPrimitive("bike_stands").getAsInt());

    return velib;
  }
}
