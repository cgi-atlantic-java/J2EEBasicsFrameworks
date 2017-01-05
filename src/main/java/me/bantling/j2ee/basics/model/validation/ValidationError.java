package me.bantling.j2ee.basics.model.validation;

import static java.util.Objects.requireNonNull;

/**
 * A single validation error
 */
public final class ValidationError {
  
  // ==== Member fields
  
  private final String modelName;
  private final String property;
  private final String message;
  
  // ==== Cons
  
  public ValidationError(
    final String modelName,
    final String property,
    final String message
  ) {
    this.modelName = requireNonNull(modelName, "modelName");
    this.property = requireNonNull(property, "id");
    this.message = requireNonNull(message, "message");
  }
  
  // ==== Accessors
  
  public final String getModelName(
  ) {
    return modelName;
  }
  
  public final String getProperty(
  ) {
    return property;
  }
  
  public final String getMessage(
  ) {
    return message;
  }
  
  // ==== Object
  
  @Override
  public int hashCode(
  ) {
    return ((
      modelName.hashCode() )
      * 31 + property.hashCode() )
      * 31 + message.hashCode();
  }
  
  @Override
  public boolean equals(
    final Object o
  ) {
    boolean equals = o == this;
    
    if ((! equals) && (o instanceof ValidationError)) {
      final ValidationError obj = (ValidationError)(o);
      
      equals = modelName.equals(modelName) &&
        property.equals(obj.property) &&
        message.equals(obj.message);
    }
    
    return equals;
  }
  
  @Override
  public String toString(
  ) {
    return new StringBuilder().
      append(ValidationError.class.getSimpleName()).
      append("[modelName=").append(modelName).
      append(", property=").append(property).
      append(", message=").append(message).
      toString();
  }
}
