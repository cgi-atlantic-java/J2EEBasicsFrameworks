package me.bantling.j2ee.basics.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A simple street address
 */
@Entity
@Table(name = "address")
public class AddressEntity extends BaseEntity {
  
  // ==== Instance fields
  
  private String line1;
  private String line2;
  private String city;
  private Country country;
  private Region region;
  
  @Column(name = "postal_code")
  private String postalCode;
  
  // ==== Accessors

  public String getLine1(
  ) {
    return line1;
  }
  
  public void setLine1(
    final String line1
  ) {
    this.line1 = line1;
  }

  public String getLine2(
  ) {
    return line2;
  }
  
  public void setLine2(
    final String line2
  ) {
    this.line2 = line2;
  }

  public String getCity(
  ) {
    return city;
  }
  
  public void setCity(
    final String city
  ) {
    this.city = city;
  }

  public Country getCountry(
  ) {
    return country;
  }
  
  public void setCountry(
    final Country country
  ) {
    this.country = country;
  }

  public Region getRegion(
  ) {
    return region;
  }
  
  public void setRegion(
    final Region region
  ) {
    this.region = region;
  }

  public String getPostalCode(
  ) {
    return postalCode;
  }
  
  public void setPostalCode(
    final String postalCode
  ) {
    this.postalCode = postalCode;
  }
  
  // ==== Object
  
  @Override
  public int hashCode(
  ) {
    return
      ((((((
      getId() ) * 31 +
      Objects.hashCode(line1) ) * 31 +
      Objects.hashCode(line2) ) * 31 +
      Objects.hashCode(city) ) * 31 +
      Objects.hashCode(country) ) * 31 +
      Objects.hashCode(region) ) * 31 +
      Objects.hashCode(postalCode);
  }
  
  @Override
  public boolean equals(
    final Object o
  ) {
    boolean equals = o == this;
    
    if ((! equals) && (o instanceof AddressEntity)) {
      final AddressEntity obj = (AddressEntity)(o);
      
      equals =
        (getId() == obj.getId()) &&
        Objects.equals(line1, obj.line1) &&
        Objects.equals(line2, obj.line2) &&
        Objects.equals(city, obj.city) &&
        Objects.equals(country, obj.country) &&
        Objects.equals(region, obj.region) &&
        Objects.equals(postalCode, obj.postalCode);
    }
    
    return equals;
  }
  
  @Override
  public String toString(
  ) {
    return new StringBuilder().
      append(AddressEntity.class.getSimpleName()).
      append("[id=").append(getId()).
      append(", line1=").append(line1).
      append(", line2=").append(line2).
      append(", city=").append(city).
      append(", country=").append(country).
      append(", region=").append(region).
      append(", postalCode=").append(postalCode).
      append(']').
      toString();
  }
}
