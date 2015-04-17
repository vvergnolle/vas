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
