package me.bantling.j2ee.basics.model.transaction;

import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.CONNECTION_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.NONE;

import java.sql.Connection;

import javax.sql.DataSource;

import me.bantling.j2ee.basics.model.exception.ModelException;

/**
 * Abstract details of accessing data via some SQL database
 */
public class JDBCConnectionFactory implements ConnectionFactory<Connection> {
  
  // ==== Instance fields
  
  private final DataSource dataSource;
  
  // ==== Cons
  
  /**
   * @param dataSource
   */
  public JDBCConnectionFactory(
    final DataSource dataSource
  ) {
    this.dataSource = dataSource;
  }
  
  // ==== ModelConnection
  
  @Override
  public Connection getConnection(
  ) {
    final Connection connection;
    
    try {
      connection = dataSource.getConnection(); 
    } catch (final Exception e) {
      throw new ModelException(CONNECTION_FAILED, NONE, e);
    }
    
    return connection;
  }
}
