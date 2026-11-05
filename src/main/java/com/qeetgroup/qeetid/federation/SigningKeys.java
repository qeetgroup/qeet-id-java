package com.qeetgroup.qeetid.federation;

import com.fasterxml.jackson.databind.JsonNode;
import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.internal.ApiRequest;
import com.qeetgroup.qeetid.internal.Json;
import com.qeetgroup.qeetid.internal.Transport;
import java.util.ArrayList;
import java.util.List;

/**
 * The issuer's signing keys. Rotation is an operator task and isn't part of the API.
 *
 * <p>Get it from its parent resource. Safe for concurrent use.
 */
public final class SigningKeys {
  private final Transport http;

  /**
   * Used by the client; get this from its parent resource.
   *
   * @param http the client's transport
   */
  public SigningKeys(Transport http) {
    this.http = http;
  }

  /**
   * Lists the issuer's signing keys: their ids, algorithms and states, never the keys.
   *
   * @return the keys
   */
  public ListResult<SigningKey> list() {
    return list(RequestOptions.none());
  }

  /**
   * Lists the issuer's signing keys, with per-call settings. See {@link #list()}.
   *
   * @param options per-call settings
   * @return the keys
   */
  public ListResult<SigningKey> list(RequestOptions options) {
    JsonNode keys = http.tree(ApiRequest.get("/v1/oidc/signing-keys"), options).path("keys");
    List<SigningKey> out = new ArrayList<>();
    keys.forEach(key -> out.add(Json.convert(key, SigningKey.class)));
    return new ListResult<>(out, false);
  }
}
