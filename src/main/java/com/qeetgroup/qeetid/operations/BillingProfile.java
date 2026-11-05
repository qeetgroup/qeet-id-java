package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Who invoices are addressed to.
 *
 * @param legalName the legal name
 * @param billingEmail the billing email
 * @param addressLine1 the address line1
 * @param addressLine2 the address line2
 * @param city the city
 * @param state the state
 * @param postalCode the postal code
 * @param country the country
 * @param taxId the tax's id
 * @param taxIdType the tax ID type
 */
public record BillingProfile(
    @JsonProperty("legal_name") String legalName,
    @JsonProperty("billing_email") String billingEmail,
    @JsonProperty("address_line1") String addressLine1,
    @JsonProperty("address_line2") String addressLine2,
    String city,
    String state,
    @JsonProperty("postal_code") String postalCode,
    String country,
    @JsonProperty("tax_id") String taxId,
    @JsonProperty("tax_id_type") String taxIdType) {}
