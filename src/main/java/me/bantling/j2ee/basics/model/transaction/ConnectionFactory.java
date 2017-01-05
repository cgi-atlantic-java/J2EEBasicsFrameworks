package me.bantling.j2ee.basics.model.transaction;

/**
 * Get a connection of some kind
 * 
 * @param <T> the type of connection object
 */
public interface ConnectionFactory<T> {
  public T getConnection(
  );
}
