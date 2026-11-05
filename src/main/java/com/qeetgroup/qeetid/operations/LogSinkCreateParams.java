package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** A destination to stream the audit log to. */
public final class LogSinkCreateParams extends Params {
  private LogSinkCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link LogSinkCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link LogSinkCreateParams}. */
  public static final class Builder extends Params.Builder<LogSinkCreateParams, Builder> {
    private Builder() {}

    /**
     * One of {@code "splunk_hec"}, {@code "datadog"}, {@code "http"}. Required.
     *
     * @param type the value
     * @return this builder
     */
    public Builder type(String type) {
      return set("type", nonNull(type, "type"));
    }

    /**
     * The endpoint. Required.
     *
     * @param endpoint the value
     * @return this builder
     */
    public Builder endpoint(String endpoint) {
      return set("endpoint", nonNull(endpoint, "endpoint"));
    }

    /**
     * The token.
     *
     * @param token the value
     * @return this builder
     */
    public Builder token(String token) {
      return setSecret("token", nonNull(token, "token"));
    }

    @Override
    public LogSinkCreateParams build() {
      require("type", "type");
      require("endpoint", "endpoint");
      return new LogSinkCreateParams(this);
    }
  }
}
