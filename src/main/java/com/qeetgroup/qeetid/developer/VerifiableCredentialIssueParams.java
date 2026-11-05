package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;
import java.util.Map;
import org.jspecify.annotations.Nullable;

/** A W3C verifiable credential to issue. */
public final class VerifiableCredentialIssueParams extends Params {
  private VerifiableCredentialIssueParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link VerifiableCredentialIssueParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link VerifiableCredentialIssueParams}. */
  public static final class Builder
      extends Params.Builder<VerifiableCredentialIssueParams, Builder> {
    private Builder() {}

    /**
     * The subject. Required.
     *
     * @param subject the value
     * @return this builder
     */
    public Builder subject(String subject) {
      return set("subject", nonNull(subject, "subject"));
    }

    /**
     * Its type. Required.
     *
     * @param type the value
     * @return this builder
     */
    public Builder type(String type) {
      return set("type", nonNull(type, "type"));
    }

    /**
     * The claims.
     *
     * @param claims the value
     * @return this builder
     */
    public Builder claims(Map<String, ?> claims) {
      return set("claims", nonNull(claims, "claims"));
    }

    /**
     * How long it lasts, in seconds.
     *
     * @param ttlSeconds the value
     * @return this builder
     */
    public Builder ttlSeconds(int ttlSeconds) {
      return set("ttl_seconds", ttlSeconds);
    }

    /**
     * One of the organisation's users the credential is about. Null sends an explicit null, which
     * clears it.
     *
     * @param userId the value
     * @return this builder
     */
    public Builder userId(@Nullable String userId) {
      return set("user_id", userId);
    }

    @Override
    public VerifiableCredentialIssueParams build() {
      require("subject", "subject");
      require("type", "type");
      return new VerifiableCredentialIssueParams(this);
    }
  }
}
