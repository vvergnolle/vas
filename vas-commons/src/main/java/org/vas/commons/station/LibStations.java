package org.vas.commons.station;

public class LibStations {

  public final Stations<Autolib> autolibs;
  public final Stations<Velib> velibs;

  public LibStations(Stations<Autolib> autolibs, Stations<Velib> velibs) {
    super();
    this.autolibs = autolibs;
    this.velibs = velibs;
  }
}
