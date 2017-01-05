package me.bantling.j2ee.basics.model.exception;

import static java.util.Objects.requireNonNull;

import java.util.List;

import me.bantling.j2ee.basics.model.validation.ValidationError;

/**
 * An exception occurs somewhere in the model
 */
public class ModelException extends RuntimeException {
  
  // ==== Static fields
  
  private static final long serialVersionUID = 1L;
  
  // ==== Instance fields
  
  private final ModelExceptionCode exceptionCode;
  private final ModelExceptionSubCode exceptionSubCode;
  private final List<ValidationError> validationErrors;
  
  // ==== Cons

  /**
   * @param exceptionCode
   * @param exceptionSubCode
   * @param validationErrors
   * @param message
   * @param cause
   */
  private ModelException(
    final ModelExceptionCode exceptionCode,
    final ModelExceptionSubCode exceptionSubCode,
    final List<ValidationError> validationErrors,
    final String message,
    final Throwable cause
  ) {
    super(message, cause);
    
    if (exceptionCode == null) {
      final NullPointerException npe = new NullPointerException("exceptionCode");
      npe.initCause(cause);
      throw npe;
    }

    if (exceptionSubCode == null) {
      final NullPointerException npe = new NullPointerException("exceptionSubCode");
      npe.initCause(cause);
      throw npe;
    }
    
    if (! exceptionCode.subCodes().contains(exceptionSubCode)) {
      throw new IllegalArgumentException(
        "The exceptionCode " + exceptionCode + " does not have a subCode of " + exceptionSubCode,
        cause
      );
    }
    
    if (exceptionSubCode.requiresMessage() && (message == null)) {
      final NullPointerException npe = new NullPointerException("message requirded for subCode " + exceptionSubCode);
      npe.initCause(cause);
      throw npe;
    }
    
    if (validationErrors != null) {
      if (exceptionCode != ModelExceptionCode.VALIDATION) {
        throw new IllegalArgumentException(
          "validation errors must be exception code " + ModelExceptionCode.VALIDATION.name()
        );
      }

      if (exceptionSubCode != ModelExceptionSubCode.NONE) {
        throw new IllegalArgumentException(
          "validation errors must be exception subCode " + ModelExceptionSubCode.NONE.name()
        );
      }
    }
    
    this.exceptionCode = exceptionCode;
    this.exceptionSubCode = exceptionSubCode;
    this.validationErrors = validationErrors;
  }

  /**
   * @param exceptionCode
   * @param exceptionSubCode
   * @param message
   * @param cause
   */
  public ModelException(
    final ModelExceptionCode exceptionCode,
    final ModelExceptionSubCode exceptionSubCode,
    final String message,
    final Throwable cause
  ) {
    this(exceptionCode, exceptionSubCode, null, message, cause);
  }

  /**
   * @param exceptionCode
   * @param message
   * @param cause
   */
  public ModelException(
    final ModelExceptionCode exceptionCode,
    final String message,
    final Throwable cause
  ) {
    this(exceptionCode, ModelExceptionSubCode.NONE, null, message, cause);
  }

  /**
   * @param exceptionCode
   * @param exceptionSubCode
   * @param message
   */
  public ModelException(
    final ModelExceptionCode exceptionCode,
    final ModelExceptionSubCode exceptionSubCode,
    final String message
  ) {
    this(exceptionCode, exceptionSubCode, null, message, null);
  }

  /**
   * @param exceptionCode
   * @param exceptionSubCode
   * @param cause
   */
  public ModelException(
    final ModelExceptionCode exceptionCode,
    final ModelExceptionSubCode exceptionSubCode,
    final Throwable cause
  ) {
    this(exceptionCode, exceptionSubCode, null, null, cause);
  }

  /**
   * @param exceptionCode
   * @param exceptionSubCode
   * @param cause
   */
  public ModelException(
    final List<ValidationError> validationErrors
  ) {
    this(
      ModelExceptionCode.VALIDATION,
      ModelExceptionSubCode.NONE,
      requireNonNull(validationErrors, "validationErrors"),
      null,
      null
    );
    
    if (validationErrors.isEmpty()) {
      throw new IllegalArgumentException("validationErrors cannot be empty");
    }
  }
  
  // ==== Accessors
  
  public ModelExceptionCode getExceptionCode(
  ) {
    return exceptionCode;
  }
  
  public ModelExceptionSubCode getExceptionSubCode(
  ) {
    return exceptionSubCode;
  }
  
  public List<ValidationError> getValidationErrors(
  ) {
    return validationErrors;
  }
}
