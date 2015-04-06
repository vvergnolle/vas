package org.vas.core.test;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.google.common.eventbus.EventBus;

@Guice(modules = TestModule.class)
public class TestEventBusAndGuice {

  @Inject
  EventBus eventBus;

  @Inject
  CountEventListener listener;

  @Test
  public void itShouldCountEventPost() {
    eventBus.post(new Event());
    eventBus.post(new SubEvent());
    eventBus.post(new SubEvent());
    eventBus.post(new Event());

    assertThat(listener.count).as("count should be equals to 4").isEqualTo(4);

    eventBus.unregister(listener);
  }
}
