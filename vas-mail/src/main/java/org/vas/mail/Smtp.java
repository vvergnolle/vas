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
