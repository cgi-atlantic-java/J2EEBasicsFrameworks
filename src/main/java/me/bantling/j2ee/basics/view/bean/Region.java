package me.bantling.j2ee.basics.view.bean;

import static me.bantling.j2ee.basics.view.bean.Country.*;

/**
 * A region (eg, province/state) of a street address
 */
public enum Region {
  
  // ==== Constants
  
  BC(CAN, "British Columbia"),
  NS(CAN, "Nova Scotia"),
  CA(USA, "California"),
  TX(USA, "Texas");
  
  // ==== Instance fields
  
  private final Country country;
  private final String toString;
  
  // ==== Cons
  
  private Region(
    final Country country,
    final String toString
  ) {
    this.country = country;
    this.toString = toString;
  }
  
  // ==== Accessors
  
  public Country getCountry(
  ) {
    return country;
  }
  
  @Override
  public String toString(
  ) {
    return toString;
  }
}
