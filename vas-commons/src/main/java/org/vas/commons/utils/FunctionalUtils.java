package org.vas.commons.utils;

public class FunctionalUtils {

  public interface Procedure {

    void invoke() throws Exception;
  }

  public static void quiet(Procedure procedure) {
    try {
      procedure.invoke();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
