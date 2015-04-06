package org.vas.boot;

import org.vas.launcher.Vas;
import org.vas.launcher.VasFactory;

public class Main {

  public static void main(String[] args) throws Exception {
    Vas vas = VasFactory.create();
    vas.start();
  }
}
