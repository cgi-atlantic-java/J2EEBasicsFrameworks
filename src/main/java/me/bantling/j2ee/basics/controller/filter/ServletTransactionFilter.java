package me.bantling.j2ee.basics.controller.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.bantling.j2ee.basics.model.transaction.Transaction;

/**
 * Begin a new transaction at the start of every servlet request, and commit or rollback at the end of the request.
 * Non-servlet requests (css, js, images, etc) will not be part of transactions.
 */
public class ServletTransactionFilter implements Filter {
  private static final Logger log = LoggerFactory.getLogger(ServletTransactionFilter.class);
  
  public static final String NAME = ServletTransactionFilter.class.getSimpleName();
  public static final ServletTransactionFilter INSTANCE = new ServletTransactionFilter();
  
  @Override
  public void init(
    final FilterConfig config
  ) throws ServletException {
    //
  }
  
  @Override
  public void doFilter(
    final ServletRequest request,
    final ServletResponse response,
    final FilterChain chain
  ) throws ServletException {
    log.debug("Starting request transaction");
    try (
      final Transaction transaction = Transaction.getTransaction();
    ) {
      try {
        chain.doFilter(request, response);
        
        log.debug("Committing request transaction");
        transaction.commit();
      } catch (final Exception e) {
        log.debug("Rolling back request transaction");
        transaction.rollback();
        
        throw e;
      }
    } catch (final Exception e) {
      throw new ServletException(e);
    }
  }
  
  @Override
  public void destroy(
  ) {
    //
  }
}
