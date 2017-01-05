package me.bantling.j2ee.basics.model.exception;

public enum ModelExceptionSubCode {
  // NO SUB CODES
  NONE,
  
  // TRANSACTION
  BEGIN,
  FLUSH,
  COMMIT,
  ROLLBACK,
  CLOSE,
  
  // DML
  INSERT,
  SELECT,
  UPDATE,
  DELETE,
  NO_ROWS,
  MULTIPLE_ROWS(true);
  
  private final boolean requiresMessage;
  
  private ModelExceptionSubCode(
    final boolean requiresMessage
  ) {
    this.requiresMessage = requiresMessage;
  }
  
  private ModelExceptionSubCode(
  ) {
    this(false);
  }
  
  public boolean requiresMessage(
  ) {
    return requiresMessage;
  }
}
