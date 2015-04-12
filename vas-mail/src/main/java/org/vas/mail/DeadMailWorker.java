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
