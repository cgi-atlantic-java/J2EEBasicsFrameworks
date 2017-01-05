package me.bantling.j2ee.basics.model.transaction;

import static me.bantling.j2ee.basics.model.exception.ModelExceptionCode.CONNECTION_FAILED;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.NONE;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import me.bantling.j2ee.basics.model.exception.ModelException;

public class JPAConnectionFactory implements ConnectionFactory<EntityManager> {
  
  // ==== Instance fields
  
  private final EntityManagerFactory entityManagerFactory;
  
  // ====  Cons
  
  public JPAConnectionFactory(
    final String persistenceUnitName
  ) {
    this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
  }
  
  @Override
  public EntityManager getConnection(
  ) {
    final EntityManager entityManager;
    
    try {
      entityManager = entityManagerFactory.createEntityManager(); 
    } catch (final Exception e) {
      throw new ModelException(CONNECTION_FAILED, NONE, e);
    }
    
    return entityManager;
  }
}
