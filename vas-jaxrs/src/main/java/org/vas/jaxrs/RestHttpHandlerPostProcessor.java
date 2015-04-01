package org.vas.jaxrs;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;

import java.util.ServiceLoader;

import org.apache.wink.server.internal.servlet.RestServlet;
import org.vas.commons.context.BootContext;
import org.vas.commons.http.HttpHandlerPostProcessor;

public class RestHttpHandlerPostProcessor implements HttpHandlerPostProcessor {

	@Override
	public void postProcess(BootContext context) {
		DeploymentInfo deploymentInfo = context.deploymentInfo();
		
		ServiceLoader
			.load(JaxrsDescriptor.class)
			.iterator()
			.forEachRemaining(descriptor -> {
				ServletInfo servletInfo = toServletInfo(descriptor);
				deploymentInfo.addServlet(servletInfo);
			});
	}

	protected ServletInfo toServletInfo(JaxrsDescriptor descriptor) {
		return Servlets
			.servlet(
					descriptor.id(),
					RestServlet.class
			)
			.addMappings(descriptor.mapping()) 
			.setLoadOnStartup(1)
			.addInitParam(
					RestServlet.APPLICATION_INIT_PARAM,
					descriptor.applicationClass().getName()
			)
			.addInitParam("requestProcessorAttribute", descriptor.id());
  }
}
