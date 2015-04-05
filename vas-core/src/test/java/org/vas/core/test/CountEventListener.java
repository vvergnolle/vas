package org.vas.core.test;

import javax.inject.Inject;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class CountEventListener {

	int count;

	@Inject
	public CountEventListener(EventBus eventBus) {
		super();
		eventBus.register(this);
	}

	@Subscribe
	public void onTwo(Event event) throws Exception {
		count++;
	}
}
