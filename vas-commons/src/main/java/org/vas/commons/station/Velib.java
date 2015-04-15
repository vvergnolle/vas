package org.vas.commons.station;

public class Velib extends LatLng {

  private int stands;
  private int dist;
  private int available;
  private int availableStands;

  public int getDist() {
    return dist;
  }

  public void setDist(int dist) {
    this.dist = dist;
  }

  public int getStands() {
    return stands;
  }

  public void setStands(int stands) {
    this.stands = stands;
  }

  public int getAvailable() {
    return available;
  }

  public void setAvailable(int available) {
    this.available = available;
  }

  public int getAvailableStands() {
    return availableStands;
  }

  public void setAvailableStands(int availableStands) {
    this.availableStands = availableStands;
  }
}
