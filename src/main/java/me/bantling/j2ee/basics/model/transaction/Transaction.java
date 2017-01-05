package me.bantling.j2ee.basics.model.transaction;

import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.CONNECTION_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.DATA_SOURCE_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.TRANSACTION_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.BEGIN;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.COMMIT;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.FLUSH;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.NONE;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.ROLLBACK;

import java.sql.Connection;

import javax.persistence.EntityManager;

import me.bantling.j2ee.basics.model.exception.ModelException;
import me.bantling.j2ee.basics.model.mao.api.MAOFactory;
import me.bantling.j2ee.basics.model.mao.jpa.JPAMAOFactory;

public class Transaction implements AutoCloseable {
  
  // ==== Static fields
  
//  private static final String HSQL_JNDI_NAME = "jdbc/mem";
  private static final String HSQL_PERSISTENCE_UNIT = "hsqldb";
  
//  static final JDBCConnectionFactory jdbcConnectionFactory;
  static final JPAConnectionFactory jpaConnectionFactory;
  
  private static final ThreadLocal<Transaction> TRANSACTION_THREAD_LOCAL = new ThreadLocal<Transaction>() {
    @Override
    protected Transaction initialValue(
    ) {
//      return new Transaction(jdbcConnectionFactory.getConnection(), new JDBCMAOFactory());
      return new Transaction(new JPAMAOFactory());
    }
  };
  
  static {
    try {
//      final InitialContext ctx = new InitialContext();
//      final DataSource dataSource = (DataSource)(ctx.lookup(HSQL_JNDI_NAME));
//      jdbcConnectionFactory = new JDBCConnectionFactory(dataSource);
      jpaConnectionFactory = new JPAConnectionFactory(HSQL_PERSISTENCE_UNIT);
    } catch (final Exception e) {
//      throw new ModelException(DATA_SOURCE_FAILED, NONE, HSQL_JNDI_NAME, e);
      throw new ModelException(DATA_SOURCE_FAILED, NONE, HSQL_PERSISTENCE_UNIT, e);
    }
  }
  
  // ==== Static methods
  
  /**
   * @return new transaction
   */
  public static Transaction getTransaction(
  ) throws ModelException {
    return TRANSACTION_THREAD_LOCAL.get();
  }

  // ==== Instance fields
  
  private final Connection jdbcConnection; 
  private EntityManager jpaConnection;
  private final MAOFactory maoFactory;
  
  // ==== Cons
  
  public Transaction(
//    final Connection jdbcConnection,
    final MAOFactory maoFactory
  ) throws ModelException {
//    this.jdbcConnection = jdbcConnection;
    this.jdbcConnection = null;
    this.maoFactory = maoFactory;
  }
  
  // ==== Impl
  
  public Connection getJDBCConnection(
  ) {
    return jdbcConnection;
  }
  
  public EntityManager getConnection(
  ) {
    if (jpaConnection == null) {
      try {
        jpaConnection = jpaConnectionFactory.getConnection();
        jpaConnection.getTransaction().begin();
      } catch (final ModelException e) {
        throw e;
      } catch (final Exception e) {
        throw new ModelException(TRANSACTION_FAILED, BEGIN, e);
      }
    }
    return jpaConnection;
  }
  
  public MAOFactory getMAOFactory(
  ) {
    return maoFactory;
  }
  
  public void flush(
  ) throws ModelException {
    try {
      jpaConnection.flush();
    } catch (final Exception e) {
      throw new ModelException(TRANSACTION_FAILED, FLUSH, e);
    }
  }
  
  public void commit(
  ) throws ModelException {
    try {
//      jdbcConnection.commit();
      jpaConnection.getTransaction().commit();
    } catch (final Exception e) {
      throw new ModelException(TRANSACTION_FAILED, COMMIT, e);
    }
  }
  
  public void rollback(
  ) throws ModelException {
    try {
//      jdbcConnection.rollback();
      jpaConnection.getTransaction().rollback();
    } catch (final Exception e) {
      throw new ModelException(TRANSACTION_FAILED, ROLLBACK,e);
    }
  }
  
  @Override
  public void close(
  ) throws ModelException {
    try {
//      jdbcConnection.close();
      jpaConnection.close();
      jpaConnection = null;
    } catch (final Exception e) {
      throw new ModelException(CONNECTION_FAILED, NONE,e);
    }
  }
}
