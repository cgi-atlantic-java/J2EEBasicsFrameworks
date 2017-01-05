package me.bantling.j2ee.basics.model.mao.api;

import me.bantling.j2ee.basics.model.entity.AddressEntity;
import me.bantling.j2ee.basics.model.exception.ModelException;

public interface AddressMAO {
  void createTable(
  ) throws ModelException;
  
  void validate(
    final AddressEntity entity
  ) throws ModelException;
  
  void insert(
    AddressEntity entity
  ) throws ModelException;
  
  AddressEntity select(
    int id
  ) throws ModelException;
  
  void update(
    AddressEntity entity
  ) throws ModelException;
  
  void delete(
    int id
  ) throws ModelException;
}
