package me.bantling.j2ee.basics.controller.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simplest servlet - just send back a hard-coded Java String
 */
@WebServlet(
  name = "HelloWorld",
  urlPatterns = "/hello",
  loadOnStartup = 1
)
public class HelloWorldServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(HelloWorldServlet.class);

  @Override
  protected void doGet(
    final HttpServletRequest request,
    final HttpServletResponse response
  ) throws ServletException, IOException {
    log.info("> doGet");
    
    response.getWriter().
      append("<!DOCTYE html>\n").
      append("<html>\n").
      append("  <head>\n").
      append("    <title>Hello World</title>\n").
      append("  </head>\n").
      append("  <body>\n").
      append("    <h1>Hello, World</h1>\n").
      append("    <p>").append(new Date().toString()).append("</p>\n").
      append("  </body>\n").
      append("</html>\n");
    
    log.info("< doGet");
  }
}
