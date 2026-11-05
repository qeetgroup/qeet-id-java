package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

/**
 * A tax invoice. Amounts are in minor units.
 *
 * @param id its id
 * @param invoiceNumber the invoice number
 * @param planCode the plan code
 * @param kind One of {@code "purchase"}, {@code "renewal"}; others may be added
 * @param status One of {@code "paid"}, {@code "open"}, {@code "void"}; others may be added
 * @param currency the currency
 * @param amountMinor the amount minor
 * @param taxableAmountMinor the taxable amount minor
 * @param taxAmountMinor the tax amount minor
 * @param taxRateBps the tax rate bps
 * @param taxType the tax type
 * @param placeOfSupply the place of supply
 * @param supplierGstin the supplier gstin
 * @param sac the sac
 * @param billedTo the billed to
 * @param periodStart the period start
 * @param periodEnd the period end
 * @param issuedAt the issued time
 * @param dueAt the due time
 * @param paidAt the paid time
 * @param emailedAt the emailed time
 */
public record Invoice(
    String id,
    @JsonProperty("invoice_number") @Nullable String invoiceNumber,
    @JsonProperty("plan_code") String planCode,
    @Nullable String kind,
    String status,
    String currency,
    @JsonProperty("amount_minor") long amountMinor,
    @JsonProperty("taxable_amount_minor") long taxableAmountMinor,
    @JsonProperty("tax_amount_minor") long taxAmountMinor,
    @JsonProperty("tax_rate_bps") int taxRateBps,
    @JsonProperty("tax_type") String taxType,
    @JsonProperty("place_of_supply") String placeOfSupply,
    @JsonProperty("supplier_gstin") @Nullable String supplierGstin,
    @Nullable String sac,
    @JsonProperty("billed_to") @Nullable BillingProfile billedTo,
    @JsonProperty("period_start") Instant periodStart,
    @JsonProperty("period_end") Instant periodEnd,
    @JsonProperty("issued_at") Instant issuedAt,
    @JsonProperty("due_at") @Nullable Instant dueAt,
    @JsonProperty("paid_at") @Nullable Instant paidAt,
    @JsonProperty("emailed_at") @Nullable Instant emailedAt) {}
