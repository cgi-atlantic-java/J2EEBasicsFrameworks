package me.bantling.j2ee.basics.view.bean;

/**
 * A country of a street address
 */
public enum Country {
  
  // ==== Constants
  
  CAN("Canada", "Province", "Postal Code"),
  USA("United States", "State", "Zip Code");
  
  // ==== Instance fields
  
  private final String toString;
  private final String regionLabel;
  private final String postalCodeLabel;
  
  // ==== Cons
  
  private Country(
    final String toString,
    final String regionLabel,
    final String postalCodeLabel
  ) {
    this.toString = toString;
    this.regionLabel = regionLabel;
    this.postalCodeLabel = postalCodeLabel;
  }
  
  // ==== Accessors
  
  @Override
  public String toString(
  ) {
    return toString;
  }
  
  public String regionLabel(
  ) {
    return regionLabel;
  }
  
  public String postalCodeLabel(
  ) {
    return postalCodeLabel;
  }
}
