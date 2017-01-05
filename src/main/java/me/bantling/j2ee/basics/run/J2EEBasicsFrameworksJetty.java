package me.bantling.j2ee.basics.run;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Slf4jRequestLog;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.Configuration.ClassList;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

/**
 * Configure Jetty and start it up to listen on a pre-defined port of {@value #PORT}.
 * This port number is chosen because some Windows machines are already listening in port 8080 as some part of IIS.
 */
public class J2EEBasicsFrameworksJetty {
  /**
   * Jetty runs on port 8180
   */
  public static final int PORT = 8180;
  
  /**
   * Jetty context path
   */
  public static final String CONTEXT_PATH = "/basics";
  
  /**
   * Configure and start Jetty
   * 
   * @return instance of running server
   * @throws Exception
   */
  public static Server startJetty(
  ) throws Exception {
    // Create the server on the specified port
    final Server server = new Server(PORT);
    
    // Enable parsing of jndi-related parts of web.xml and jetty-env.xml
    ClassList classlist = ClassList.setServerDefault(server);
    classlist.addAfter(
      FragmentConfiguration.class.getName(),
      EnvConfiguration.class.getName(),
      PlusConfiguration.class.getName()
    );
    
    // Use a jetty-env.xml to specify a datasource as a JNDI entry
    final EnvConfiguration envConfiguration = new EnvConfiguration();
    envConfiguration.setJettyEnvXml(Paths.get("src/main/webapp/WEB-INF/jetty-env.xml").toUri().toURL());
    
    // The context for our application
    final WebAppContext webappContext = new WebAppContext();
    
    // Set the directory to represent the top level of a WAR structure - EG, this directory contains WEB-INF
    webappContext.setResourceBase("src/main/webapp");
    
    /*
     * Configure this context to be able to handle annotations (EG @WebServlet), jetty-env.xml, WEB-INF,
     * and WEB-INF/web.xml
     */
    webappContext.setConfigurations(new Configuration[] {
      new AnnotationConfiguration(),
      envConfiguration,
      new WebInfConfiguration(),
      new WebXmlConfiguration()
    });
    
    // In Jetty, the ROOT can be set to "/" without any warnings, we use /basics
    webappContext.setContextPath(CONTEXT_PATH);
    
    /*
     * Configure the context with the location of compiled class directories to scan for annotations (EG, @WebServlet).
     * Merely adding the above AnnotationConfiguration only makes it possible to parse annotations, without setting a
     * location on disk to scan, no annotations never get processed. 
     */
    final URL classes = J2EEBasicsFrameworksJetty.class.getProtectionDomain().getCodeSource().getLocation();
    webappContext.getMetaData().setWebInfClassesDirs(
      Arrays.asList(Resource.newResource(classes))
    );
    
    // Turn off directory displays by default servlet (similar to Apache listings) - user gets 403 forbidden errors
    webappContext.setInitParameter(
      "org.eclipse.jetty.servlet.Default.dirAllowed",
      "false"
    );
    
    // Configure access logging the new way in Jetty 9.3 using Slf4jRequestLog rather than logback-access
    final Slf4jRequestLog accessLog = new Slf4jRequestLog();
    accessLog.setLoggerName("container.access");
    accessLog.setLogDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS X");
    accessLog.setLogLocale(Locale.ENGLISH);
    accessLog.setLogTimeZone("Z");
    accessLog.setExtended(true);
    accessLog.setLogCookies(true);
    server.setRequestLog(accessLog);
    
    /*
     * Just pass our context directly - it is possible to pass another type of handler that can contain
     * multiple contexts
     */
    server.setHandler(webappContext);
    
    // Start the server
    server.start();
    
    return server;
  }
  
  /**
   * Main method to start Jetty and wait for it to terminate
   * 
   * @param args ignored
   * @throws Exception
   */
  public static void main(
    final String[] args
  ) throws Exception {
    // Start Jetty and wait for it to die
    startJetty().join();
  }
}
