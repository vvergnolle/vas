package org.vas.launcher;

import java.util.Properties;

import org.vas.inject.Services;

public interface Vas {
  ServerConf conf();

  Env env();

  void start() throws Exception;

  void start(Env env) throws Exception;

  void stop() throws Exception;

  void restart() throws Exception;

  void restart(Env env) throws Exception;

  Properties properties();

  Services services();
}
