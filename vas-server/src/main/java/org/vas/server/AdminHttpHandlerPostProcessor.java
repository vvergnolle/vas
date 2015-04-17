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
          Servlets.servlet("logback", ViewStatusMessagesServlet.class).setEnabled(true).setAsyncSupported(true)
            .setLoadOnStartup(1).addMappings("/"));

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

      ServletInfo servletInfo = Servlets.servlet("admin-jaxrs", AdminServlet.class).setEnabled(true)
        .setAsyncSupported(true).setLoadOnStartup(1).addMappings("/admin/jaxrs");

      deploymentInfo.addServlet(servletInfo);
    }
  }
}
