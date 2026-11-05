package com.qeetgroup.qeetid.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.jspecify.annotations.Nullable;

/** The wire shapes of lists. */
final class Envelopes {
  private Envelopes() {}

  /** A cursor-paginated list: {@code {"items": [...], "next_cursor": "..."}}. */
  record PageEnvelope<T>(List<T> items, @JsonProperty("next_cursor") @Nullable String nextCursor) {}

  /** A whole list: {@code {"items": [...], "truncated": true}}. */
  record ListEnvelope<T>(List<T> items, boolean truncated) {}

  /** The client-credentials token endpoint's answer, as far as the client needs it. */
  record TokenAnswer(
      @JsonProperty("access_token") String accessToken,
      @JsonProperty("expires_in") long expiresIn) {

    @Override
    public String toString() {
      return "TokenAnswer[accessToken=[redacted], expiresIn=" + expiresIn + "]";
    }
  }
}
