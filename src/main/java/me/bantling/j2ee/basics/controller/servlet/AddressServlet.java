package me.bantling.j2ee.basics.controller.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.bantling.j2ee.basics.model.entity.AddressEntity;
import me.bantling.j2ee.basics.model.exception.ModelException;
import me.bantling.j2ee.basics.model.mao.api.AddressMAO;
import me.bantling.j2ee.basics.model.transaction.Transaction;
import me.bantling.j2ee.basics.view.bean.AddressBean;
import me.bantling.j2ee.basics.view.bean.Country;
import me.bantling.j2ee.basics.view.bean.Region;
import me.bantling.j2ee.basics.view.form.ObjectCreators;

/**
 * Servlet to handle reading and writing {@link AddressBean} objects
 */
@WebServlet(
  name = "Address",
  urlPatterns = "/address",
  loadOnStartup = 1
)
public class AddressServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(AddressServlet.class);
  
  /**
   * Create the address table and insert one row into it
   */
  @Override
  public void init(
  ) throws ServletException {
    // Create an address table and insert one row into it that we can search for
    try (
      final Transaction transaction = Transaction.getTransaction();
    ) {
      final AddressMAO addressMAO = transaction.getMAOFactory().getAddressMAO();
      
      try {
        addressMAO.createTable();
        
        final AddressBean bean = new AddressBean(
          1,
          "7071 Bayers Rd",
          null,
          "Halifax",
          Country.CAN,
          Region.NS,
          "B3L 2C2"
        );
        addressMAO.insert(bean.toEntity());
        
        transaction.commit();
      } catch (final Exception e) {
        transaction.rollback();
        throw e;
      }
    } catch (final Exception e) {
      throw new ServletException(e);
    }
    
    log.info("initialized HSQL address table");
  }

  /**
   * Get the single address for display
   */
  @Override
  protected void doGet(
    final HttpServletRequest request,
    final HttpServletResponse response
  ) throws ServletException {
    try {
      log.info("> doGet");

      try {
        final AddressMAO addressMAO = Transaction.getTransaction().getMAOFactory().getAddressMAO();
        
        // Retrieve the one address
        final AddressEntity entity = addressMAO.select(1);
        
        final AddressBean bean = new AddressBean(entity);
        log.debug("Retrieved Address = {}", bean);
        
        // Provide the address to the view
        request.setAttribute("address", bean);
        
        Transaction.getTransaction().flush();
      } catch (final ModelException e) {
        // If we get any error from the model, display it
        request.setAttribute("error", e);
      }
    
      // Render the view
      request.getRequestDispatcher("/WEB-INF/jsp/address.jsp").forward(request, response);
      
      log.info("< doGet");
    } catch (final Exception e) {
      throw new ServletException(e);
    }
  }
  
  /**
   * Update the single address
   */
  @Override
  protected void doPost(
    final HttpServletRequest request,
    final HttpServletResponse response
  ) throws ServletException {
    try {
      log.info("> doPost");
            
      // Get the address from the form parameters
      final AddressBean bean = ObjectCreators.ADDRESS_CREATOR.create(request);
      log.debug("Address = {}", bean);
      
      try {
        // Attempt to update the address
        final AddressMAO addressMAO = Transaction.getTransaction().getMAOFactory().getAddressMAO();
        addressMAO.update(bean.toEntity());
        Transaction.getTransaction().flush();
      } catch (final ModelException e) {
        // If we get any error from the model, display it
        request.setAttribute("error", e);
      }
      
      /*
       * Regardless of whether or not we got an error, provide the given address to the view.
       * If the given address is invalid, it lets the user see the invalid data and correct it.
       */
      request.setAttribute("address", bean);
    
      // Render the address
      request.getRequestDispatcher("/WEB-INF/jsp/address.jsp").forward(request, response);
  
      log.info("< doPost");
    } catch (final Exception e) {
      throw new ServletException(e);
    }
  }
  
  @Override
  public void destroy(
  ) {
    log.info("destroyed");
  }
}
