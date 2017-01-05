package me.bantling.j2ee.basics.controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.bantling.j2ee.basics.run.J2EEBasicsFrameworksJetty;

/**
 * Log the details of each servlet context: contextPath, servlet names, and servlet mappings. The mappings are shown as
 * the URL needed to access the servlet locally, assuming the server runs on the same machine as the client.
 * 
 * It is assumed that the server is listening on the port specified by {@link J2EEBasicsFrameworksJetty#PORT}. 
 */
@WebListener
public class ServletContextLogger implements ServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(ServletContextLogger.class);
  
  @Override
  public void contextInitialized(
    final ServletContextEvent evt
  ) {
    final StringBuilder sb = new StringBuilder();
    sb.
      append("Initialized context ").
      append(evt.getServletContext().getContextPath()).
      append(" with following servlets:");
    
    for (final ServletRegistration servletReg : evt.getServletContext().getServletRegistrations().values()) {
      sb.
        append("\n").
        append(servletReg.getName()).
        append(" => ");
      
      boolean first = true;
      for (final String mapping : servletReg.getMappings()) {
        sb.
          append(first ? "" : ", ").
          append("http://localhost:").
          append(J2EEBasicsFrameworksJetty.PORT).
          append(evt.getServletContext().getContextPath()).
          append(mapping.charAt(0) == '/' ? "" : "/").
          append(mapping);
        
        first = false;
      }
    }
    
    log.info(sb.toString());
  }

  @Override
  public void contextDestroyed(
    final ServletContextEvent evt
  ) {
    log.info("Destroyed context " + evt.getServletContext().getContextPath());
  }
}
