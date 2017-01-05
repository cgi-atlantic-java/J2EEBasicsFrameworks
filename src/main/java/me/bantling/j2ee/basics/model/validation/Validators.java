package me.bantling.j2ee.basics.model.validation;

import java.util.HashMap;
import java.util.Map;

import me.bantling.j2ee.basics.model.entity.AddressEntity;
import me.bantling.j2ee.basics.model.entity.BaseEntity;
import me.bantling.j2ee.basics.model.entity.Country;

/**
 * Implementations of {@link Validator}
 */
public class Validators {
  
  // ==== No Cons
  
  private Validators(
  ) {
    //
  }
  
  // ==== Static fields
  
  private static final Map<Class<? extends BaseEntity>, Validator<? extends BaseEntity>> VALIDATORS;
  
  static {
    VALIDATORS = new HashMap<>();
    
    VALIDATORS.put(
      AddressEntity.class,
      new ValidatorBuilder<>(
        AddressEntity.class
      ).
        nonNull("entity", t -> t).
        intGreaterThan("id", t -> t.getId(), 0).
        nonEmptyString("line1", t -> t.getLine1(), 32).
        nullOrNonEmptyString("line2", t -> t.getLine2(), 32).
        nonEmptyString("city", t -> t.getCity(), 32).
        nonNull("country", t -> t.getCountry()).
        nonNull("region", t -> t.getRegion()).
        test("region", t -> t.getCountry() == t.getRegion().getCountry(), "Must be a Region of the specified Country").
        validCDNPostalCode(t -> t.getCountry() == Country.CAN, "postalCode", t -> t.getPostalCode()).
        validUSAZipCode(t -> t.getCountry() == Country.USA, "postalCode", t -> t.getPostalCode()).
      end()
    );
  }
  
  public static <T> Validator<T> get(
    final Class<T> entityType
  ) {
    @SuppressWarnings("unchecked")
    final Validator<T> validator = (Validator<T>)(VALIDATORS.get(entityType));
    return validator;
  }
}
