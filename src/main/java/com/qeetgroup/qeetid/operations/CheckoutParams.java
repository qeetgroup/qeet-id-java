package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** A checkout for a plan. */
public final class CheckoutParams extends Params {
  private CheckoutParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link CheckoutParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link CheckoutParams}. */
  public static final class Builder extends Params.Builder<CheckoutParams, Builder> {
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

    /**
     * ISO 3166-1 alpha-2, for tax.
     *
     * @param country the value
     * @return this builder
     */
    public Builder country(String country) {
      return set("country", nonNull(country, "country"));
    }

    /**
     * Where to send the person after paying. Required.
     *
     * @param successUrl the value
     * @return this builder
     */
    public Builder successUrl(String successUrl) {
      return set("success_url", nonNull(successUrl, "successUrl"));
    }

    /**
     * Where to send the person if they give up. Required.
     *
     * @param cancelUrl the value
     * @return this builder
     */
    public Builder cancelUrl(String cancelUrl) {
      return set("cancel_url", nonNull(cancelUrl, "cancelUrl"));
    }

    @Override
    public CheckoutParams build() {
      require("plan_code", "planCode");
      require("currency", "currency");
      require("success_url", "successUrl");
      require("cancel_url", "cancelUrl");
      return new CheckoutParams(this);
    }
  }
}
