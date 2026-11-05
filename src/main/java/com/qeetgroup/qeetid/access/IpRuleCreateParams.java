package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** A new address rule. */
public final class IpRuleCreateParams extends Params {
  private IpRuleCreateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link IpRuleCreateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link IpRuleCreateParams}. */
  public static final class Builder extends Params.Builder<IpRuleCreateParams, Builder> {
    private Builder() {}

    /**
     * One of {@code "allow"}, {@code "deny"}. Required.
     *
     * @param action the value
     * @return this builder
     */
    public Builder action(String action) {
      return set("action", nonNull(action, "action"));
    }

    /**
     * An address range, such as {@code 203.0.113.0/24}. Required.
     *
     * @param cidr the value
     * @return this builder
     */
    public Builder cidr(String cidr) {
      return set("cidr", nonNull(cidr, "cidr"));
    }

    /**
     * The label. Required.
     *
     * @param label the value
     * @return this builder
     */
    public Builder label(String label) {
      return set("label", nonNull(label, "label"));
    }

    @Override
    public IpRuleCreateParams build() {
      require("action", "action");
      require("cidr", "cidr");
      require("label", "label");
      return new IpRuleCreateParams(this);
    }
  }
}
