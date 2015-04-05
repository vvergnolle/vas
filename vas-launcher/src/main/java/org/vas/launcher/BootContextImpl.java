package org.vas.launcher;

import io.undertow.Handlers;
import io.undertow.attribute.ConstantExchangeAttribute;
import io.undertow.attribute.ExchangeAttribute;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.bean.MsgBean;
import org.vas.commons.context.BootContext;
import org.vas.inject.Services;

class BootContextImpl implements BootContext {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected final Services services;
	protected final Properties properties;
	protected final PathHandler pathHandler;
	protected final DeploymentInfo deploymentInfo;
	protected final ObjectMapper objectMapper = new ObjectMapper();
	protected final Map<String, ExchangeAttribute> headers = new HashMap<>();
	protected final Set<ExceptionHandlerHolder> exceptionHandlerEntries = new HashSet<>();
	protected final Deque<ConditionalHandlerHolder> conditionalHandlers = new LinkedList<>();

	public BootContextImpl(Properties properties, PathHandler pathHandler, DeploymentInfo deploymentInfo,
	    Services serviceContainer) { 
		super();
		this.properties = properties;
		this.pathHandler = pathHandler;
		this.deploymentInfo = deploymentInfo;
		this.services = serviceContainer;
	}

	@Override
	public Properties properties() {
		return properties;
	}

	@Override
	public <T> T getService(Class<T> klass) {
		return services.get(klass);
	}

	@Override
	public void inject(Object object) {
		services.inject(object);
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
	public void bindException(Class<? extends Throwable> exception, int status) {
		bindException(exception, status, false);
	}
	
	@Override
	public void bindException(Class<? extends Throwable> throwable, int status, boolean flushMessage) {
		if(flushMessage) {
			bindException(throwable, new FlushMessageExceptionHandler(status, objectMapper));
		}
		else {
			bindException(throwable, new StatusExceptionHandler(status));
		}
	}
	
	@Override
	public void bindException(Class<? extends Throwable> throwable, HttpHandler handler) {
	  exceptionHandlerEntries.add(new ExceptionHandlerHolder(throwable, handler));
	}
	
	public void addHeader(String header, ExchangeAttribute value) {
		headers.put(header, value);
	}

	@Override
	public void addPredicate(int priority, Predicate predicate, HttpHandler truePredicate) {
		if(priority < 0) {
			throw new IllegalStateException("The priority must be > 0");
		}

		conditionalHandlers.push(new ConditionalHandlerHolder(priority, predicate, truePredicate));
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
		for (ConditionalHandlerHolder condition : sortedPredicates()) {
			if (handler == null) {
				handler = Handlers.predicate(condition.predicate, condition.httpHandler, pathHandler);
			} else {
				handler = Handlers.predicate(condition.predicate, condition.httpHandler, handler);
			}
		}

		for(Entry<String, ExchangeAttribute> header : this.headers.entrySet()) {
			handler = Handlers.header(handler, header.getKey(), header.getValue());
		}
		
		/*
		 * Build exception handler if exception handler entries has been registered
		 */
		if(!exceptionHandlerEntries.isEmpty()) {
			ExceptionHandler exceptionHandler = Handlers.exceptionHandler(handler);
			for(ExceptionHandlerHolder ehe : exceptionHandlerEntries) {
				exceptionHandler.addExceptionHandler(ehe.throwable, ehe.handler);
			}

			handler = new HttpHandler() {

				@Override
				public void handleRequest(HttpServerExchange exchange) throws Exception {
					if(exchange.isInIoThread()) {
						exchange.dispatch(this);
						return;
					}

					exceptionHandler.handleRequest(exchange);
				}
			};
		}

		return handler;
	}

	/**
	 * Sort by priority
	 */
	protected List<ConditionalHandlerHolder> sortedPredicates() {
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
 */
class ConditionalHandlerHolder {

	Integer priority;
	Predicate predicate;
	HttpHandler httpHandler;

	public ConditionalHandlerHolder(Integer priority, Predicate predicate, HttpHandler httpHandler) {
		super();
		this.priority = priority;
		this.predicate = predicate;
		this.httpHandler = httpHandler;
	}
}

class ExceptionHandlerHolder {
	
	public final Class<? extends Throwable> throwable;
	public final HttpHandler handler;

	public ExceptionHandlerHolder(Class<? extends Throwable> throwable, HttpHandler handler) {
	  super();
	  this.throwable = throwable;
	  this.handler = handler;
  }
	
	@Override
	public int hashCode() {
	  return throwable.getName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		
		if(obj instanceof ExceptionHandlerHolder) {
			ExceptionHandlerHolder holder = (ExceptionHandlerHolder) obj;
			return throwable == holder.throwable;
		}

	  return super.equals(obj);
	}
}

class StatusExceptionHandler implements HttpHandler {
		
	final int status;
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	public StatusExceptionHandler(int status) {
	  super();
	  this.status = status;
  }

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
	  Throwable throwable = exchange.getAttachment(ExceptionHandler.THROWABLE);
	  if(logger.isErrorEnabled()) {
	  	logger.error("HttpHandler intercept an error", throwable);
	  }

		exchange.setResponseCode(status);
	}		
}

class FlushMessageExceptionHandler extends StatusExceptionHandler {

	final ObjectMapper objectMapper;

	public FlushMessageExceptionHandler(int status, ObjectMapper objectMapper) {
	  super(status);
	  this.objectMapper = objectMapper;
  }

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
	  super.handleRequest(exchange);
	  
	  String message = exchange.getAttachment(ExceptionHandler.THROWABLE).getMessage();
	  byte[] bytes = objectMapper.writeValueAsBytes(MsgBean.of(message));
	  exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
	}
}
