package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** The plan and currency to switch the subscription to. */
public final class SubscriptionUpdateParams extends Params {
  private SubscriptionUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link SubscriptionUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link SubscriptionUpdateParams}. */
  public static final class Builder extends Params.Builder<SubscriptionUpdateParams, Builder> {
    private Builder() {}

    /**
     * The plan's code. Required.
     *
     * @param planCode the value
     * @return this builder
     */
    public Builder planCode(String planCode) {
      return set("plan_code", nonNull(planCode, "planCode"));
    }

    /**
     * An ISO 4217 currency code, such as {@code INR}. Required.
     *
     * @param currency the value
     * @return this builder
     */
    public Builder currency(String currency) {
      return set("currency", nonNull(currency, "currency"));
    }

    @Override
    public SubscriptionUpdateParams build() {
      require("plan_code", "planCode");
      require("currency", "currency");
      return new SubscriptionUpdateParams(this);
    }
  }
}
