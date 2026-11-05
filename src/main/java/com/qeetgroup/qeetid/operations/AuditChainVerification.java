package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

/**
 * The result of walking the hash chain now. A broken chain names the first bad row.
 *
 * @param ok the ok
 * @param rowsChecked the rows checked
 * @param lastVerifiedId the last verified's id
 * @param brokenAtId the broken at's id
 * @param brokenReason the broken reason
 * @param reasonCode One of {@code "audit.chain_prev_hash_mismatch"}, {@code
 *     "audit.chain_row_hash_mismatch"}; others may be added
 */
public record AuditChainVerification(
    boolean ok,
    @JsonProperty("rows_checked") int rowsChecked,
    @JsonProperty("last_verified_id") @Nullable String lastVerifiedId,
    @JsonProperty("broken_at_id") @Nullable String brokenAtId,
    @JsonProperty("broken_reason") @Nullable String brokenReason,
    @JsonProperty("reason_code") @Nullable String reasonCode) {}
