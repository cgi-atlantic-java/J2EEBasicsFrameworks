package me.bantling.j2ee.basics.model.mao.jdbc;

import me.bantling.j2ee.basics.model.mao.api.AddressMAO;
import me.bantling.j2ee.basics.model.mao.api.MAOFactory;

public class JDBCMAOFactory implements MAOFactory {
  
  // ==== Instance fields
  
  private final AddressMAO addressMAO;
  
  // ==== Cons
  
  {
    this.addressMAO = new AddressJDBCMAO();
  }
  
  @Override
  public AddressMAO getAddressMAO(
  ) {
    return addressMAO;
  }
}
