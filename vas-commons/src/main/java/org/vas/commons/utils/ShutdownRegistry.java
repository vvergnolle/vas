package org.vas.commons.utils;

import static org.vas.commons.utils.FunctionalUtils.quiet;

import java.io.Closeable;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class ShutdownRegistry {

  protected final Set<Closeable> closeables = Collections.synchronizedSet(Sets.newHashSet());
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Thread shutdownThread;

  {
    init();
  }

  private void init() {
    shutdownThread = new Thread(this::shutdown);
    Runtime.getRuntime().addShutdownHook(shutdownThread);
  }

  private void shutdown() {
    if(logger.isInfoEnabled()) {
      logger.info("Shutdown {} resources", closeables.size());
    }

    synchronized (closeables) {
      closeables.iterator().forEachRemaining(closeable -> quiet(closeable::close));
    }
  }

  public void add(Closeable closeable) {
    closeables.add(closeable);
  }
}
