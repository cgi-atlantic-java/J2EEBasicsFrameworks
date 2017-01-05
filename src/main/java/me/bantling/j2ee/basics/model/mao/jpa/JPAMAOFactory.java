package me.bantling.j2ee.basics.model.mao.jpa;

import me.bantling.j2ee.basics.model.mao.api.AddressMAO;
import me.bantling.j2ee.basics.model.mao.api.MAOFactory;

public class JPAMAOFactory implements MAOFactory {
  
  // ==== Instance fields
  
  private final AddressMAO addressMAO;
  
  // ==== Cons
  
  {
    this.addressMAO = new AddressJPAMAO();
  }
  
  @Override
  public AddressMAO getAddressMAO(
  ) {
    return addressMAO;
  }
}
