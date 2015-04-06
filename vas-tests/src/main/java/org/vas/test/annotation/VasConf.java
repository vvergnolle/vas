package org.vas.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VasConf {

  int port() default 8080;

  int ioThreads() default 4;

  int bufferSize() default 8096;

  String host() default "localhost";
}
