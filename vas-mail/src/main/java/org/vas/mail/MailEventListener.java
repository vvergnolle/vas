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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.event.StartEvent;
import org.vas.worker.WorkerService;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

public class MailEventListener {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  protected MailDispatcher dispatcher;

  @Inject
  protected WorkerService workerService;

  @Inject
  public void autoRegister(EventBus eventBus) {
    eventBus.register(this);
  }

  @Subscribe
  public void onMail(SendMailEvent event) {
    if(logger.isTraceEnabled()) {
      logger.trace("New mail to dispatch");
    }

    dispatcher.dispatch(event.mail);
  }

  @Subscribe
  public void onStart(StartEvent event) {
    if(logger.isDebugEnabled()) {
      logger.debug("Start mail dispatcher worker");
    }

    workerService.start(dispatcher);
  }
}
