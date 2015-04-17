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
package org.vas.launcher;

import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;

import org.vas.commons.context.BootContext;
import org.vas.commons.http.HttpHandlerDescriptor;
import org.vas.commons.http.HttpHandlerPostProcessor;
import org.vas.commons.io.FileLoader;

final class VasHelper {

  private VasHelper() {}

  static DeploymentInfo deploymentInfo() {
    return Servlets.deployment().setClassLoader(classLoader()).setDeploymentName(Const.WAR_NAME)
      .setContextPath(Const.CONTEXT_NAME);
  }

  static void lookupHandlerPostProcessors(BootContext context) {
    ServiceLoader.load(HttpHandlerPostProcessor.class).iterator()
      .forEachRemaining(processor -> processor.postProcess(context));
  }

  static Properties loadProperties(Env env) throws IOException {
    String propertiesLocation = env.propertiesLocation;
    if(propertiesLocation == null || propertiesLocation.isEmpty()) {
      System.err.println("The application configuration file must be indicated");
      System.exit(Const.ERR_FAIL_TO_LOCATE_APP_CONFIG_FILE);
    }

    return new FileLoader(propertiesLocation).toProperties();
  }

  static List<HttpHandlerDescriptor> lookupHandlerDescriptors(PathHandler handler) {
    List<HttpHandlerDescriptor> descriptors = new ArrayList<>();
    ServiceLoader.load(HttpHandlerDescriptor.class).iterator()
      .forEachRemaining(descriptor -> handler.addPrefixPath(descriptor.uri(), instanciate(descriptor.httpHandler())));

    return descriptors;
  }

  static ClassLoader classLoader() {
    return VasHelper.class.getClassLoader();
  }

  static <T> T instanciate(Class<T> klass) {
    try {
      return (T) klass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
