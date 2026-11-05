package com.qeetgroup.qeetid.identity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * What {@code delete} answers: who was deleted, when and by whom.
 *
 * @param id its id
 * @param deletedAt the deleted time
 * @param deletedBy the deleted by
 */
public record UserDeletion(
    String id,
    @JsonProperty("deleted_at") Instant deletedAt,
    @JsonProperty("deleted_by") @Nullable String deletedBy) {}
