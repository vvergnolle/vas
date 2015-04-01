package org.vas.server;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.api.ServletInfo;

import javax.servlet.ServletException;

import org.apache.wink.server.internal.servlet.AdminServlet;
import org.vas.commons.context.BootContext;
import org.vas.commons.http.HttpHandlerPostProcessor;

import ch.qos.logback.classic.ViewStatusMessagesServlet;

/**
 * Register admin resources
 *
 */
public class AdminHttpHandlerPostProcessor implements HttpHandlerPostProcessor {

	@Override
	public void postProcess(BootContext context) {
		ServletContainer servletContainer = Servlets.defaultContainer();

		deployLogs(context, servletContainer);
		deployJaxrs(context);
	}

	protected void deployLogs(BootContext context, ServletContainer servletContainer) {
	  String adminLogs = context.properties().getProperty("vas.admin.logs", "false");
		if(Boolean.valueOf(adminLogs)) {
			DeploymentInfo deploymentInfo = Servlets
			    .deployment()
			    .setContextPath("/")
			    .setDeploymentName("logback.war")
			    .setClassLoader(getClass().getClassLoader())
			    .addServlet(
		        Servlets
		        	.servlet("logback", ViewStatusMessagesServlet.class)
		        	.setEnabled(true)
		        	.setAsyncSupported(true)
		          .setLoadOnStartup(1)
		          .addMappings("/")
			     );

			DeploymentManager deploymentManager = servletContainer.addDeployment(deploymentInfo);
			deploymentManager.deploy();

			try {
				context.pathHandler().addPrefixPath("/admin/logs", deploymentManager.start());
			} catch (ServletException e) {
				throw new RuntimeException(e);
			}
		}
  }

	protected void deployJaxrs(BootContext context) {
	  String adminJaxrs = context.properties().getProperty("vas.admin.jaxrs", "false");
		if(Boolean.valueOf(adminJaxrs)) {
			DeploymentInfo deploymentInfo = context.deploymentInfo();

			ServletInfo servletInfo = Servlets
	     	.servlet("admin-jaxrs", AdminServlet.class)
	     	.setEnabled(true)
	     	.setAsyncSupported(true)
	      .setLoadOnStartup(1)
	      .addMappings("/admin/jaxrs");
			 
			 deploymentInfo.addServlet(servletInfo);
		}
  }
}
