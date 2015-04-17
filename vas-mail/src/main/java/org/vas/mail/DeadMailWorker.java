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
package org.vas.mail;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeadMailWorker extends TimerTask {

  private static final int MAIL_CAPACITY = 20;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final MailWorker mailWorker;
  private final Timer timer = new Timer("dead-mails-worker");
  private final ExecutorService writerWorker = Executors.newSingleThreadExecutor();
  private final Set<Mail> mails = Collections.synchronizedSet(new LinkedHashSet<>(MAIL_CAPACITY));

  public DeadMailWorker(MailWorker mailWorker) {
    super();
    this.mailWorker = mailWorker;
  }

  void push(Exception e, Mail mail) {
    writerWorker.execute(() -> {
      if(logger.isErrorEnabled()) {
        logger.error("Error occured when trying to send a mail - mail marked as dead", e);
      }

      synchronized (mails) {
        mails.add(mail);
      }
    });
  }

  void schedule(long delay, long period) {
    timer.schedule(this, delay, period);
  }

  void flush() {
    synchronized (mails) {
      mails.iterator().forEachRemaining(mailWorker::send);
    }
  }

  @Override
  public void run() {
    flush();
  }

  public void stop() {
    timer.cancel();
    writerWorker.shutdown();
    flush();
  }
}
