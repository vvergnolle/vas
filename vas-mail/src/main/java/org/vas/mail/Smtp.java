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
package org.vas.mail;

import java.util.Properties;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class Smtp {

  public final String user;
  public final String pwd;
  public final String host;
  public final int port;
  public final boolean ssl;
  public final boolean tls;
  public final boolean tlsRequired;

  public Smtp(Properties properties) {
    super();

    user = properties.getProperty("vas.smtp.user", "");
    if(user == null) {
      throw new IllegalStateException("Smtp user must be specified");
    }

    pwd = properties.getProperty("vas.smtp.pwd", "");
    if(pwd == null) {
      throw new IllegalStateException("Smtp password must be specified");
    }

    host = properties.getProperty("vas.smtp.host");
    if(host == null || host.isEmpty()) {
      throw new IllegalStateException("Smtp host must be specified");
    }

    port = Integer.valueOf(properties.getProperty("vas.smtp.port"));
    if(port <= 0) {
      throw new IllegalStateException("Smtp port must be > 0");
    }

    ssl = Boolean.valueOf(properties.getProperty("vas.smtp.ssl", "true"));
    tls = Boolean.valueOf(properties.getProperty("vas.smtp.tls", "true"));
    tlsRequired = Boolean.valueOf(properties.getProperty("vas.smtp.tlsRequired", "false"));
  }

  public Email emptyEmail() {
    SimpleEmail email = new SimpleEmail();

    email.setAuthentication(user, pwd);
    email.setSSLOnConnect(ssl);
    email.setStartTLSEnabled(tls);
    email.setStartTLSRequired(tlsRequired);
    email.setHostName(host);
    email.setSmtpPort(port);

    return email;
  }
}
