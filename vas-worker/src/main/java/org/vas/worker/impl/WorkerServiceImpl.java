package org.vas.worker.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.vas.commons.utils.ShutdownRegistry;
import org.vas.worker.WorkerService;

public final class WorkerServiceImpl implements WorkerService, Closeable {

  final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(6);

  @Inject
  public void init(ShutdownRegistry shutdownRegistry) {
    shutdownRegistry.add(this);
  }

  public void start(Runnable task) {
    executorService.execute(task);
  }

  public <T> CompletableFuture<T> start(Supplier<T> job) {
    return CompletableFuture.supplyAsync(job, executorService);
  }

  @Override
  public void schedule(Runnable runnable, int initialDelay, int period, TimeUnit unit) {
    executorService.scheduleWithFixedDelay(runnable, initialDelay, period, unit);
  }

  @Override
  public void close() throws IOException {
    executorService.shutdown();
  }
}
