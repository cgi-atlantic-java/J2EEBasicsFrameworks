package me.bantling.j2ee.basics.controller.listener;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.bantling.j2ee.basics.controller.filter.ServletTransactionFilter;

/**
 * Automatically add the {@link ServletTransactionFilter} filter to every servlet 
 */
@WebListener
public class ServletContextTransactionFilter implements ServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(ServletContextTransactionFilter.class);
  
  @Override
  public void contextInitialized(
    final ServletContextEvent evt
  ) {
    // Get our package prefix
    
    final String thisPackage = getClass().getPackage().getName();
    final int firstDotIndex = thisPackage.indexOf('.');
    final int secondDotIndex = thisPackage.indexOf('.', firstDotIndex + 1);
    final String ourPackagePrefix = thisPackage.substring(0, secondDotIndex);
    log.debug("Using package prefix of {} to identify our servlets", ourPackagePrefix);
    
    // Get names of all of our servlets within this context
    
    final Set<String> servletNames = new HashSet<>();
    
    for (final ServletRegistration servletReg : evt.getServletContext().getServletRegistrations().values()) {
      if (servletReg.getClassName().startsWith(ourPackagePrefix)) {
        servletNames.add(servletReg.getName());
      }
    }

    log.info(
      new StringBuilder().
        append("Adding ").
        append(ServletTransactionFilter.class.getName()).
        append(" filter to requests of the following servlets: ").
        append(servletNames).
        toString()
    );
    
    // Register a new filter
    final FilterRegistration filterReg =
      evt.getServletContext().addFilter(ServletTransactionFilter.NAME, ServletTransactionFilter.INSTANCE);
    
    /*
     * Map it to requests for all the servlets within the context.
     * Put this filter first, as other filters may need a transaction. 
     */
    filterReg.addMappingForServletNames(
      EnumSet.of(DispatcherType.REQUEST),
      false,
      servletNames.toArray(new String[servletNames.size()])
    );
  }

  @Override
  public void contextDestroyed(
    final ServletContextEvent evt
  ) {
    //
  }
}
