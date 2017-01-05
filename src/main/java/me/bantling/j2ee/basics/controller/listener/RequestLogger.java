package me.bantling.j2ee.basics.controller.listener;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Log the details of each request: method, contextPath, pathInfo, pathTranslated, queryString, requestURI, requestURL,
 * servletPath, parameters, and headers.
 * 
 * The parameters are logged with double quotes around them, to make it possible to see if there are leading and/or
 * trailing whitespace characters, or if a value is only whitespace, etc. If a name or value shows as "null", it means
 * that it was literally transmitted as a String containing the word null.
 */
@WebListener
public class RequestLogger implements ServletRequestListener {
  private static final Logger log = LoggerFactory.getLogger(RequestLogger.class);
  
  /**
   * Log the request
   * 
   * @param evt
   */
  @Override
  public void requestInitialized(
    final ServletRequestEvent evt
  ) {
    final HttpServletRequest request = (HttpServletRequest)(evt.getServletRequest());
    final TreeMap<String, String[]> sortedParameters = new TreeMap<>(request.getParameterMap());
    
    final StringBuilder sb = new StringBuilder();
    sb.
      append("Request").
      append("\nmethod: ").
      append(request.getMethod());
    
    sb.
      append("\ncontextPath = ").
      append(request.getContextPath()).
      append("\npathInfo = ").
      append(request.getPathInfo()).
      append("\npathTranslated = ").
      append(request.getPathTranslated()).
      append("\nqueryString = ").
      append(request.getQueryString()).
      append("\nrequestURI = ").
      append(request.getRequestURI()).
      append("\nrequestURL = ").
      append(request.getRequestURL().toString()).
      append("\nservletPath = ").
      append(request.getServletPath());
    
    sb.append("\nParameters:");
    
    for (final Map.Entry<String, String[]> e : sortedParameters.entrySet()) {
      sb.
        append("\n  ").
        append(e.getKey()).
        append('=');
      
      final String[] array = e.getValue();
      if (array.length == 0) {
        sb.append("[]");
      } else if (array.length == 1) {
        sb.
          append('"').
          append(array[0]).
          append('"');
      } else {
        boolean firstValue = true;
        sb.append('[');
        
        for (final String value : array) {
          sb.
            append(firstValue ? "" : ", ").
            append('"').
            append(value).
            append('"');
        }
        
        sb.append(']');
      }
    }
    
    sb.append("\nHeaders:");
    
    for (final Enumeration<String> enumer = request.getHeaderNames(); enumer.hasMoreElements();) {
      final String headerName = enumer.nextElement();
      final String headerValue = request.getHeader(headerName);
      
      sb.
        append("\n  ").
        append(headerName).
        append('=').
        append(headerValue);
    }
    
    log.debug(sb.toString());
  }
  
  @Override
  public void requestDestroyed(
    final ServletRequestEvent evt
  ) {
    //
  }
}
