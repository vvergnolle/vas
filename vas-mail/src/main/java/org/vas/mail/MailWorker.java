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

import static org.vas.commons.utils.FunctionalUtils.quiet;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.utils.ShutdownRegistry;

import com.google.inject.Inject;

public class MailWorker implements Runnable, MailDispatcher, Closeable {

  private static final int MAIL_CAPACITY = 50;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected final Smtp smtp;
  protected final DeadMailWorker deadMailWorker;
  protected final AtomicBoolean stopped = new AtomicBoolean(true);
  protected final ExecutorService writeService = Executors.newSingleThreadExecutor();
  protected final ArrayBlockingQueue<Mail> mails = new ArrayBlockingQueue<Mail>(MAIL_CAPACITY);

  public MailWorker(Smtp smtp) {
    super();
    this.smtp = smtp;
    this.deadMailWorker = new DeadMailWorker(this);
  }

  @Inject
  public void autoRegister(ShutdownRegistry registry) {
    registry.add(this);
  }

  @Override
  public void run() {
    if(stopped.get()) {
      stopped.set(false);
      // Shedule each 15min - but 1min for the first time
      deadMailWorker.schedule(1000 * 60, 1000 * 60 * 15);
    }

    while (!stopped.get()) {
      Mail email = null;
      try {
        email = mails.take();
        send(email);
      } catch (InterruptedException e) {
        throw new IllegalStateException(e);
      } catch (Exception e) {
        deadMailWorker.push(e, email);
      }
    }
  }

  @Override
  public void dispatch(Mail mail) {
    /**
     * Do the insert in another thread to avoid the current thread to be blocked
     * if the queue is full and wait for spaces.
     */
    writeService.execute(() -> {
      quiet(() -> mails.put(mail));
    });
  }

  @Override
  public void close() throws IOException {
    stopped.set(true);
    writeService.shutdown();
    mails.forEach(this::send); // Send pending mails
    deadMailWorker.stop();
  }

  void send(Mail mail) {
    if(logger.isTraceEnabled()) {
      logger.trace("New mail to send - {}", mail.subject);
    }

    Email email = smtp.emptyEmail();
    email.setSubject(mail.subject);
    try {
      email.setFrom(mail.from);
      email.setTo(Arrays.asList(new InternetAddress(mail.to)));
      email.setMsg(mail.body);

      if(logger.isDebugEnabled()) {
        logger.debug("Send mail {}", mail.subject);
      }

      email.send();
    } catch (EmailException | AddressException e) {
      throw new RuntimeException(e);
    }
  }
}
