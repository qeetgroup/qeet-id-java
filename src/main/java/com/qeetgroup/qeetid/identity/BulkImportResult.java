package com.qeetgroup.qeetid.identity;

import java.util.List;

/**
 * The per-row outcome of a bulk create or import.
 *
 * @param succeeded the succeeded
 * @param failed the failed
 * @param created the created
 * @param errors the errors
 */
public record BulkImportResult(
    int succeeded, long failed, List<Created> created, List<BulkImportRowError> errors) {

  /**
   * Created.
   *
   * @param line the line
   * @param email the email address
   * @param id its id
   */
  public record Created(int line, String email, String id) {}
}
