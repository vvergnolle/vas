package org.vas.auth;

import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.IdentityManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.LoginConfig;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.ServletInfo;
import io.undertow.servlet.api.WebResourceCollection;
import io.undertow.servlet.spec.HttpServletRequestImpl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vas.commons.context.BootContext;
import org.vas.commons.http.HttpHandlerPostProcessor;
import org.vas.domain.repository.User;
import org.vas.domain.repository.UserRepository;

/**
 * Attach security to the application
 * 
 */
public class AuthHttpHandlerPostProcessor implements HttpHandlerPostProcessor {

  @Override
  public void postProcess(BootContext context) {
    boolean statelessAuth = statelessAuth(context);
    DeploymentInfo deploymentInfo = context.deploymentInfo();

    deploymentInfo.addAuthenticationMechanism(RestAuthenticationMechanism.MECHANISM_NAME, (name, formParserFactory,
      properties) -> new RestAuthenticationMechanism(statelessAuth, context.getService(UserRepository.class)));

    LoginConfig loginConfig = new LoginConfig(RestAuthenticationMechanism.MECHANISM_NAME, "vas-db-realm");
    deploymentInfo.setLoginConfig(loginConfig);

    deploymentInfo.setIdentityManager(context.getService(IdentityManager.class));
    deploymentInfo.addSecurityConstraint(new SecurityConstraint().addRoleAllowed(User.ROLE).addWebResourceCollection(
      new WebResourceCollection().addUrlPattern("/*")));

    logoutServlet(context, deploymentInfo);
  }

  protected boolean statelessAuth(BootContext context) {
    Object object = context.properties().get("vas.auth.stateless");
    if(object == null) {
      return false;
    } else {
      return Boolean.valueOf(object.toString());
    }
  }

  protected void logoutServlet(BootContext context, DeploymentInfo deploymentInfo) {
    String logoutServlet = context.properties().getProperty(LogoutServlet.ENABLE_PROPERTY, "true");
    if(Boolean.valueOf(logoutServlet)) {
      ServletInfo servletInfo = Servlets.servlet(LogoutServlet.ID, LogoutServlet.class).addMapping(
        LogoutServlet.LOGOUT_URI);

      deploymentInfo.addServlet(servletInfo);
    }
  }
}

class LogoutServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  static final String ID = "users-logout";
  static final String LOGOUT_URI = "/users/logout";
  static final String ENABLE_PROPERTY = "vas.user.logout.default";

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if(logger.isTraceEnabled()) {
      logger.trace("Logout {}", req.getUserPrincipal().getName());
    }

    HttpServletRequestImpl impl = (HttpServletRequestImpl) req;
    SecurityContext securityContext = impl.getExchange().getSecurityContext();
    securityContext.logout();
  }
}
