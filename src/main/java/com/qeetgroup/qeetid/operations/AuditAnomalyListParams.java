package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** A filter and a limit for the audit anomalies. */
public final class AuditAnomalyListParams extends Params {
  private AuditAnomalyListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AuditAnomalyListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AuditAnomalyListParams}. */
  public static final class Builder extends Params.Builder<AuditAnomalyListParams, Builder> {
    private Builder() {}

    /**
     * One of {@code "open"}, {@code "resolved"}.
     *
     * @param status the value
     * @return this builder
     */
    public Builder status(String status) {
      return set("status", nonNull(status, "status"));
    }

    /**
     * The limit.
     *
     * @param limit the value
     * @return this builder
     */
    public Builder limit(int limit) {
      return set("limit", limit);
    }

    @Override
    public AuditAnomalyListParams build() {
      return new AuditAnomalyListParams(this);
    }
  }
}
