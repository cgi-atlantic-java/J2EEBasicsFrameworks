package me.bantling.j2ee.basics.model.exception;

import static java.util.Objects.requireNonNull;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.BEGIN;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.COMMIT;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.DELETE;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.FLUSH;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.INSERT;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.MULTIPLE_ROWS;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.NONE;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.NO_ROWS;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.ROLLBACK;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.SELECT;
import static me.bantling.j2ee.basics.model.exception.ModelExceptionSubCode.UPDATE;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public enum ModelExceptionCode {
  MAO_IMPLEMENTATION(NONE),
  DATA_SOURCE_FAILED(true, NONE),
  CONNECTION_FAILED(NONE),
  TRANSACTION_FAILED(BEGIN, FLUSH, COMMIT, ROLLBACK),
  DDL_FAILED(true, NONE),
  DML_FAILED(true, INSERT, SELECT, UPDATE, DELETE, NO_ROWS, MULTIPLE_ROWS),
  VALIDATION(NONE);

  private final boolean requiresMessage;
  private final Set<ModelExceptionSubCode> subCodes;
  
  private ModelExceptionCode(
    final boolean requiresMessage,
    final ModelExceptionSubCode firstSubCode,
    final ModelExceptionSubCode... moreSubCodes
  ) {
    requireNonNull(firstSubCode, "firstSubCode");
    requireNonNull(moreSubCodes, "moreSubCodes");
    
    final List<ModelExceptionSubCode> subCodes_ = new LinkedList<>();
    subCodes_.add(firstSubCode);
    Collections.addAll(subCodes_, moreSubCodes);
    
    if (subCodes_.contains(null)) {
      throw new IllegalArgumentException(
        "The subCodes cannot contain nulls"
      );
    }
    
    if (subCodes_.size() != 1 + moreSubCodes.length) {
      throw new IllegalArgumentException(
        "The subCodes cannot contain duplicates"
      );
    }
    
    if (subCodes_.contains(NONE) && (subCodes_.size() > 1)) {
      throw new IllegalArgumentException(
        "The subCodes cannot contains NONE and other subCodes"
      );
    }
    
    this.subCodes = Collections.unmodifiableSet(new HashSet<>(subCodes_));
    this.requiresMessage = requiresMessage || this.subCodes.stream().anyMatch(c -> c.requiresMessage());
  }
  
  private ModelExceptionCode(
    final ModelExceptionSubCode firstSubCode,
    final ModelExceptionSubCode... moreSubCodes
  ) {
    this(false, firstSubCode, moreSubCodes);
  }
  
  public boolean requiresMessage(
  ) {
    return requiresMessage;
  }
  
  public Set<ModelExceptionSubCode> subCodes(
  ) {
    return subCodes;
  }
}
