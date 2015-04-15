package org.vas.commons.station;

public class Autolib extends LatLng {

  private int tiers;
  private int autolib;
  private int abri;

  public int getAbri() {
    return abri;
  }

  public int getAutolib() {
    return autolib;
  }

  public int getTiers() {
    return tiers;
  }

  public void setTiers(int tiers) {
    this.tiers = tiers;
  }

  public void setAutolib(int autolib) {
    this.autolib = autolib;
  }

  public void setAbri(int abri) {
    this.abri = abri;
  }

}
