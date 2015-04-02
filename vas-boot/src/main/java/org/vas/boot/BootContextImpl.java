package org.vas.boot;

import io.undertow.Handlers;
import io.undertow.attribute.ConstantExchangeAttribute;
import io.undertow.attribute.ExchangeAttribute;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import org.vas.commons.context.BootContext;
import org.vas.inject.ServiceContainer;

public class BootContextImpl implements BootContext {

	final Properties properties;
	final PathHandler pathHandler;
	final DeploymentInfo deploymentInfo;
	final ServiceContainer serviceContainer;
	final Map<String, ExchangeAttribute> headers = new HashMap<>();
	final Deque<ConditionalHandler> conditionalHandlers = new LinkedList<>();

	public BootContextImpl(Properties properties, PathHandler pathHandler, DeploymentInfo deploymentInfo,
	    ServiceContainer serviceContainer) {
		super();
		this.properties = properties;
		this.pathHandler = pathHandler;
		this.deploymentInfo = deploymentInfo;
		this.serviceContainer = serviceContainer;
	}

	@Override
	public Properties properties() {
		return properties;
	}

	@Override
	public <T> T getService(Class<T> klass) {
		return serviceContainer.get(klass);
	}

	@Override
	public void inject(Object object) {
		serviceContainer.inject(object);
	}

	@Override
	public PathHandler pathHandler() {
		return pathHandler;
	}
	
	@Override
	public void addHeader(String header, String value) {
		addHeader(header, new ConstantExchangeAttribute(value));
	}
	
	@Override
	public void addHeader(String header, ExchangeAttribute value) {
		headers.put(header, value);
	}

	@Override
	public void addPredicate(int priority, Predicate predicate, HttpHandler truePredicate) {
		if(priority < 0) {
			throw new IllegalStateException("The priority must be > 0");
		}

		conditionalHandlers.push(new ConditionalHandler(priority, predicate, truePredicate));
	}

	@Override
	public void addNotPredicate(int priority, Predicate predicate, HttpHandler truePredicate) {
		addPredicate(priority, Predicates.not(predicate), truePredicate);
	}

	@Override
	public DeploymentInfo deploymentInfo() {
		return deploymentInfo;
	}

	HttpHandler rootHttpHandler() {
		if (conditionalHandlers.isEmpty()) {
			return pathHandler;
		}

		/*
		 * Build a new handler from predicates
		 */
		HttpHandler handler = null;
		for (ConditionalHandler condition : sortedPredicates()) {
			if (handler == null) {
				handler = Handlers.predicate(condition.predicate, condition.httpHandler, pathHandler);
			} else {
				handler = Handlers.predicate(condition.predicate, condition.httpHandler, handler);
			}
		}
		
		for(Entry<String, ExchangeAttribute> header : this.headers.entrySet()) {
			handler = Handlers.header(handler, header.getKey(), header.getValue());
		}
 
		return handler;
	}

	/**
	 * Sort by priority
	 */
	private List<ConditionalHandler> sortedPredicates() {
		return conditionalHandlers
			.stream()
			.sorted(
				(e, o) -> o.priority.compareTo(o.priority)
			)
			.collect(Collectors.toList());
  }
}

/**
 * Hold the predicate handler
 * 
 */
class ConditionalHandler {

	Integer priority;
	Predicate predicate;
	HttpHandler httpHandler;

	public ConditionalHandler(Integer priority, Predicate predicate, HttpHandler httpHandler) {
		super();
		this.priority = priority;
		this.predicate = predicate;
		this.httpHandler = httpHandler;
	}
}
