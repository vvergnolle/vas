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
package org.vas.test.listener;

import static org.vas.commons.utils.FunctionalUtils.quiet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.vas.inject.Services;
import org.vas.launcher.Env;
import org.vas.launcher.ServerConf;
import org.vas.launcher.Vas;
import org.vas.launcher.VasFactory;
import org.vas.test.TestModule;
import org.vas.test.annotation.VasBoot;
import org.vas.test.annotation.VasConf;
import org.vas.test.rest.TestRestModule;

/**
 * TestNG test listener
 * 
 * Handle {@link Vas} start & stop.
 */
public class VasServerTestListener implements ITestListener {

  protected static final Object DEFAULT = new Object();

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected final Map<Object, Vas> servers = new HashMap<>();
  protected Services defaultServiceContainer;

  @Override
  public void onStart(ITestContext context) {
    Set<Object> instances = uniqInstances(context.getAllTestMethods());
    if(logger.isTraceEnabled()) {
      logger.trace("Instances {}", Arrays.asList(instances));
    }

    for (Object instance : instances) {
      VasBoot vasBoot = vasBoot(instance);
      if(vasBoot == null) {
        if(logger.isDebugEnabled()) {
          logger.debug("No @VasBoot for the test {} ... skip", instance.getClass());
        }
        continue;
      }

      if(!vasBoot.custom()) {
        checkDefaultVas();
        defaultServiceContainer.inject(instance);
        continue;
      }

      // Create custom vas
      Env env = buildEnv(vasBoot);
      ServerConf conf = buildConf(vasBoot.conf());
      Vas vas = VasFactory.create(conf);
      quiet(() -> vas.start(env));

      // Process injection
      inject(vas, instance);
    }
  }

  protected void inject(Vas vas, Object instance) {
    createServiceContainer(vas).inject(instance);
  }

  protected void checkDefaultVas() {
    Vas vas = servers.get(DEFAULT);
    if(vas == null) {
      buildDefaultVas();
    }
  }

  protected void buildDefaultVas() {
    Vas vas = VasFactory.create();
    servers.put(DEFAULT, vas);
    quiet(() -> vas.start());
    defaultServiceContainer = createServiceContainer(vas);
  }

  protected Services createServiceContainer(Vas vas) {
    return vas.services().child(new TestModule(vas), new TestRestModule());
  }

  protected Set<Object> uniqInstances(ITestNGMethod[] allTestMethods) {
    Set<Object> instances = new HashSet<>();
    for (ITestNGMethod method : allTestMethods) {
      instances.add(method.getInstance());
    }

    return instances;
  }

  @Override
  public void onFinish(ITestContext context) {
    servers.forEach((testInstance, server) -> quiet(server::stop));
    if(logger.isDebugEnabled()) {
      logger.debug("Vas shutdown");
    }
  }

  protected VasBoot vasBoot(Object testInstance) {
    VasBoot vasBoot = null;
    for (Class<?> klass = testInstance.getClass(); klass != Object.class && vasBoot == null; klass = klass
      .getSuperclass()) {
      vasBoot = klass.getAnnotation(VasBoot.class);
    }

    return vasBoot;
  }

  protected ServerConf buildConf(VasConf vasConf) {
    return new ServerConf(vasConf.host(), vasConf.port(), vasConf.ioThreads(), vasConf.bufferSize());
  }

  protected Env buildEnv(VasBoot vasBoot) {
    return new Env(vasBoot.env().propertiesLocation(), vasBoot.profile());
  }

  @Override
  public void onTestStart(ITestResult result) {}

  @Override
  public void onTestSuccess(ITestResult result) {}

  @Override
  public void onTestFailure(ITestResult result) {}

  @Override
  public void onTestSkipped(ITestResult result) {}

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
}
