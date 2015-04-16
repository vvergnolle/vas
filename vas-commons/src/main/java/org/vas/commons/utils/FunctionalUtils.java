package org.vas.commons.utils;

public class FunctionalUtils {

  public interface Procedure0 {

    void invoke() throws Exception;
  }

  public interface Procedure1<T> {

    T invoke() throws Exception;
  }

  public static void quiet(Procedure0 procedure) {
    try {
      procedure.invoke();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T quiet(Procedure1<T> procedure) {
    try {
      return procedure.invoke();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
