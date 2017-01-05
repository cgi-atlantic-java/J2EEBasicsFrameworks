package me.bantling.j2ee.basics.model.validation;

import static java.util.Objects.requireNonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;

import me.bantling.j2ee.basics.model.exception.ModelException;

/**
 * Build a {@link Validator} from zero or more validations. A single value can have multiple validations, and multiple
 * values can be validated.
 */
public class ValidatorBuilder<T> {
  private static final Pattern VALID_CDN_POSTAL_CODE = Pattern.compile("[A-Z][0-9][A-Z] [0-9][A-Z][0-9]");
  private static final Pattern VALID_USA_ZIP_CODE = Pattern.compile("[0-9]{5}(-[0-9]{4})?");
  
  private final String modelName;
  private final List<Function<T, ValidationError>> validations;
  
  public ValidatorBuilder(
    final Class<T> model
  ) {
    this.modelName = model.getSimpleName();
  }
  
  {
    this.validations = new LinkedList<>();
  }
  
  public ValidatorBuilder<T> nonNull(
    final String name,
    final Function<T, ?> value
  ) {
    requireNonNull(name, "name");
    requireNonNull(value, "value");
    
    validations.add(t -> value.apply(t) != null ? null : new ValidationError(modelName, name, "Cannot be null"));
    
    return this;
  }
  
  public ValidatorBuilder<T> nullOrNonEmptyString(
    final String name,
    final Function<T, String> value,
    final int maxLength
  ) {
    requireNonNull(name, "name");
    requireNonNull(value, "value");
    
    validations.add(t -> {
      final ValidationError error;
      final String str = value.apply(t);
      
      if (str == null) {
        error = null;
      } else if (str.length() == 0) {
        error = new ValidationError(modelName, name, "Cannot be empty");
      } else if (str.length() > maxLength) {
        error = new ValidationError(modelName, name, "Length must be <= ".concat(Integer.toString(maxLength)));
      } else {
        error = null;
      }
      
      return error;
    });
    
    return this;
  }
  
  public ValidatorBuilder<T> nonEmptyString(
    final String name,
    final Function<T, String> value,
    final int maxLength
  ) {
    requireNonNull(name, "name");
    requireNonNull(value, "value");
    
    validations.add(t -> {
      final ValidationError error;
      final String str = value.apply(t);
      
      if ((str == null) || (str.length() == 0)) {
        error = new ValidationError(modelName, name, "Cannot be empty");
      } else if (str.length() > maxLength) {
        error = new ValidationError(modelName, name, "Length must be <= ".concat(Integer.toString(maxLength))); 
      } else {
        error = null;
      }
      
      return error;
    });
    
    return this;
  }
  
  public ValidatorBuilder<T> intGreaterThan(
    final String name,
    final ToIntFunction<T> value,
    final int min
  ) {
    requireNonNull(name, "name");
    requireNonNull(value, "value");
    
    validations.add(
      t -> value.applyAsInt(t) > min ?
        null :
        new ValidationError(modelName, name, "Must be >= ".concat(Integer.toString(min)))
    );
      
    return this;
  }
  
  public ValidatorBuilder<T> validCDNPostalCode(
    final Predicate<T> isCanada,
    final String name,
    final Function<T, String> value
  ) {
    requireNonNull(isCanada, "isCanada");
    requireNonNull(name, "name");
    requireNonNull(value, "value");
    
    validations.add(t -> {
      final String s = value.apply(t);
      
      return
        (! isCanada.test(t)) ||
        (
          (s != null) &&
          VALID_CDN_POSTAL_CODE.matcher(s).matches()
        ) ?
        null :
        new ValidationError(modelName, name, "Invalid Canadian postal code");
    });
    
    return this;
  }
  
  public ValidatorBuilder<T> validCDNPostalCode(
    final String name,
    final Function<T, String> value
  ) {
    return validCDNPostalCode(t -> true, name, value);
  }
  
  public ValidatorBuilder<T> validUSAZipCode(
    final Predicate<T> isUSA,
    final String name,
    final Function<T, String> value
  ) {
    requireNonNull(isUSA, "isUSA");
    requireNonNull(name, "name");
    requireNonNull(value, "value");
    
    validations.add(t -> {
      final String s = value.apply(t);
      
      return
        (! isUSA.test(t)) ||
        (
          (s != null) &&
          VALID_USA_ZIP_CODE.matcher(s).matches()
        ) ?
        null :
        new ValidationError(modelName, name, "Invalid US zip code");
    });
    
    return this;
  }
  
  public ValidatorBuilder<T> validUSAZipCode(
    final String name,
    final Function<T, String> str
  ) {
    return validUSAZipCode(t -> true, name, str);
  }
  
  public ValidatorBuilder<T> test(
    final String name,
    final Predicate<T> test,
    final String message
  ) {
    validations.add(
      t -> test.test(t) ? null : new ValidationError(modelName, name, message)
    );
    
    return this;
  }
  
  public Validator<T> end(
  ) {
    return t -> {
      final List<ValidationError> errors = new LinkedList<>();
      
      for (final Function<T, ValidationError> validation : validations) {
        final ValidationError error = validation.apply(t);
        
        if (error != null) {
          errors.add(error);
        }
      }
      
      if (! errors.isEmpty()) {
        throw new ModelException(errors);
      }
    };
  }
}
