package com.qeetgroup.qeetid.developer;

import com.qeetgroup.qeetid.Params;
import java.util.List;

/** A new AI agent, accountable to a sponsor. */
public final class AgentCreateParams extends Params {
  private AgentCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link AgentCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link AgentCreateParams}. */
  public static final class Builder extends Params.Builder<AgentCreateParams, Builder> {
    private Builder() {}

    /**
     * Its name. Required.
     *
     * @param name the value
     * @return this builder
     */
    public Builder name(String name) {
      return set("name", nonNull(name, "name"));
    }

    /**
     * The person accountable for the agent. Required. Required.
     *
     * @param sponsorUserId the value
     * @return this builder
     */
    public Builder sponsorUserId(String sponsorUserId) {
      return set("sponsor_user_id", nonNull(sponsorUserId, "sponsorUserId"));
    }

    /**
     * The scopes.
     *
     * @param scopes the value
     * @return this builder
     */
    public Builder scopes(List<String> scopes) {
      return set("scopes", listOf(scopes));
    }

    /**
     * The token ttl seconds.
     *
     * @param tokenTtlSeconds the value
     * @return this builder
     */
    public Builder tokenTtlSeconds(int tokenTtlSeconds) {
      return set("token_ttl_seconds", tokenTtlSeconds);
    }

    @Override
    public AgentCreateParams build() {
      require("name", "name");
      require("sponsor_user_id", "sponsorUserId");
      return new AgentCreateParams(this);
    }
  }
}
