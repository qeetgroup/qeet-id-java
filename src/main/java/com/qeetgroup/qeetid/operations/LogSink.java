package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A destination the audit log is streamed to (a SIEM). Its token is never returned.
 *
 * @param id its id
 * @param type One of {@code "splunk_hec"}, {@code "datadog"}, {@code "http"}; others may be added
 * @param endpoint the endpoint
 * @param enabled whether it is on
 * @param lastForwardedAt the last forwarded time
 * @param lastError the last error
 * @param createdAt when it was created
 */
public record LogSink(
    String id,
    String type,
    String endpoint,
    boolean enabled,
    @JsonProperty("last_forwarded_at") @Nullable Instant lastForwardedAt,
    @JsonProperty("last_error") @Nullable String lastError,
    @JsonProperty("created_at") Instant createdAt) {}
