/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Vincent Vergnolle
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.vas.worker.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.vas.commons.utils.ShutdownRegistry;
import org.vas.worker.WorkerService;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public final class WorkerServiceImpl implements WorkerService, Closeable {

  final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(6);
  final Scheduler scheduler = Schedulers.from(executorService);

  @Inject
  public void init(ShutdownRegistry shutdownRegistry) {
    shutdownRegistry.add(this);
  }

  @Override
  public <T> Observable<T> observable(Callable<T> callable) {
    return Observable.from(executorService.submit(callable), scheduler);
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
