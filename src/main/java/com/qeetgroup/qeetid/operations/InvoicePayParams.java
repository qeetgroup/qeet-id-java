package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** Where to send the person paying an invoice afterwards. */
public final class InvoicePayParams extends Params {
  private InvoicePayParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link InvoicePayParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link InvoicePayParams}. */
  public static final class Builder extends Params.Builder<InvoicePayParams, Builder> {
    private Builder() {}

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
    public InvoicePayParams build() {
      require("success_url", "successUrl");
      require("cancel_url", "cancelUrl");
      return new InvoicePayParams(this);
    }
  }
}
