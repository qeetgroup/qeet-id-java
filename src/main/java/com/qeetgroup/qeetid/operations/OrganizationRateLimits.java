package com.qeetgroup.qeetid.operations;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * The organization's rate limits per scope, with the defaults and ceilings they sit within.
 *
 * @param organization the organization
 * @param user the user
 * @param apiKey the API key
 * @param defaults Read-only.
 * @param ceiling the ceiling
 * @param minRate the min rate
 * @param builtIn Read-only: the platform's fixed limits, such as sign-in attempts per address.
 */
public record OrganizationRateLimits(
    @Nullable RateLimitBucket organization,
    @Nullable RateLimitBucket user,
    @JsonProperty("api_key") @Nullable RateLimitBucket apiKey,
    @Nullable Defaults defaults,
    @Nullable Ceiling ceiling,
    @JsonProperty("min_rate") @Nullable Double minRate,
    @JsonProperty("built_in") List<BuiltIn> builtIn) {

  /**
   * Read-only.
   *
   * @param organization the organization
   * @param user the user
   * @param apiKey the API key
   */
  public record Defaults(
      RateLimitBucket organization,
      RateLimitBucket user,
      @JsonProperty("api_key") RateLimitBucket apiKey) {}

  /**
   * Ceiling.
   *
   * @param organization the organization
   * @param user the user
   * @param apiKey the API key
   */
  public record Ceiling(
      RateLimitBucket organization,
      RateLimitBucket user,
      @JsonProperty("api_key") RateLimitBucket apiKey) {}

  /**
   * Read-only: the platform's fixed limits, such as sign-in attempts per address.
   *
   * @param name its name
   * @param per One of {@code "ip"}, {@code "user"}, {@code "address"}; others may be added
   * @param applies the applies
   * @param rate the rate
   * @param capacity the capacity
   */
  public record BuiltIn(String name, String per, String applies, double rate, int capacity) {}
}
