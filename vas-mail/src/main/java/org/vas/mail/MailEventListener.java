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
