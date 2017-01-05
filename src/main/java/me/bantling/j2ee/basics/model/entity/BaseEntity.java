package me.bantling.j2ee.basics.model.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
  
  // ==== Instance fields
  
  @Id
  private int id;
  
  // ==== Accessors
  
  public final int getId(
  ) {
    return id;
  }
  
  public void setId(
    final int id
  ) {
    this.id = id;
  }
}
