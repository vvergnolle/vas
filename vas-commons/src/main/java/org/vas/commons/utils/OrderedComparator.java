package org.vas.commons.utils;

import java.util.Comparator;

public class OrderedComparator implements Comparator<Ordered> {

  @Override
  public int compare(Ordered o1, Ordered o2) {
    return Integer.compare(o1.order(), o2.order());
  }
}
