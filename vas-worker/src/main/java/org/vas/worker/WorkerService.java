package org.vas.worker;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface WorkerService {

  void start(Runnable task);

  <T> CompletableFuture<T> start(Supplier<T> job);

  void schedule(Runnable runnable, int initialDelay, int period, TimeUnit unit);
}
