package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** Who invoices are addressed to. Only the fields given change. */
public final class BillingProfileUpdateParams extends Params {
  private BillingProfileUpdateParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link BillingProfileUpdateParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link BillingProfileUpdateParams}. */
  public static final class Builder extends Params.Builder<BillingProfileUpdateParams, Builder> {
    private Builder() {}

    /**
     * The legal name.
     *
     * @param legalName the value
     * @return this builder
     */
    public Builder legalName(String legalName) {
      return set("legal_name", nonNull(legalName, "legalName"));
    }

    /**
     * The billing email.
     *
     * @param billingEmail the value
     * @return this builder
     */
    public Builder billingEmail(String billingEmail) {
      return set("billing_email", nonNull(billingEmail, "billingEmail"));
    }

    /**
     * The address line1.
     *
     * @param addressLine1 the value
     * @return this builder
     */
    public Builder addressLine1(String addressLine1) {
      return set("address_line1", nonNull(addressLine1, "addressLine1"));
    }

    /**
     * The address line2.
     *
     * @param addressLine2 the value
     * @return this builder
     */
    public Builder addressLine2(String addressLine2) {
      return set("address_line2", nonNull(addressLine2, "addressLine2"));
    }

    /**
     * The city.
     *
     * @param city the value
     * @return this builder
     */
    public Builder city(String city) {
      return set("city", nonNull(city, "city"));
    }

    /**
     * The state.
     *
     * @param state the value
     * @return this builder
     */
    public Builder state(String state) {
      return set("state", nonNull(state, "state"));
    }

    /**
     * The postal code.
     *
     * @param postalCode the value
     * @return this builder
     */
    public Builder postalCode(String postalCode) {
      return set("postal_code", nonNull(postalCode, "postalCode"));
    }

    /**
     * The country.
     *
     * @param country the value
     * @return this builder
     */
    public Builder country(String country) {
      return set("country", nonNull(country, "country"));
    }

    /**
     * The tax's id.
     *
     * @param taxId the value
     * @return this builder
     */
    public Builder taxId(String taxId) {
      return set("tax_id", nonNull(taxId, "taxId"));
    }

    /**
     * The tax ID type.
     *
     * @param taxIdType the value
     * @return this builder
     */
    public Builder taxIdType(String taxIdType) {
      return set("tax_id_type", nonNull(taxIdType, "taxIdType"));
    }

    @Override
    public BillingProfileUpdateParams build() {
      return new BillingProfileUpdateParams(this);
    }
  }
}
