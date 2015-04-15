package org.vas.commons.station;

import java.util.List;

import com.google.common.collect.Lists;

public class Stations<T extends LatLng> {

  private int hits;
  private int rows;
  private List<T> stations = Lists.newArrayList();

  public void addStation(T station) {
    stations.add(station);
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public void setHits(int hits) {
    this.hits = hits;
  }

  public int getHits() {
    return hits;
  }

  public List<T> getStations() {
    return stations;
  }
}
