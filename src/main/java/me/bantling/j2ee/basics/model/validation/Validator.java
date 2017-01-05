package me.bantling.j2ee.basics.model.validation;

import me.bantling.j2ee.basics.model.exception.ModelException;

@FunctionalInterface
public interface Validator<T> {
  public void validate(
    final T entity
  ) throws ModelException;
}
