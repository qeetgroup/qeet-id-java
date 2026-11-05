package com.qeetgroup.qeetid.jwt;

import com.qeetgroup.qeetid.exception.ApiError;
import com.qeetgroup.qeetid.exception.ApiException;
import com.qeetgroup.qeetid.exception.NetworkException;
import com.qeetgroup.qeetid.exception.TimeoutException;
import com.qeetgroup.qeetid.internal.Json;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/** Fetches the authorization server's discovery document. */
public final class Discovery {
  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  private Discovery() {}

  /**
   * Fetches {@code {baseUrl}/.well-known/openid-configuration}.
   *
   * @param baseUrl the API's base URL, such as {@code https://api.id.qeet.in}
   * @return the document
   * @throws NetworkException when the server can't be reached
   * @throws ApiException when it answers with something other than a discovery document
   */
  public static DiscoveryDocument fetch(String baseUrl) {
    return fetch(baseUrl, HttpClient.newBuilder().connectTimeout(TIMEOUT).build());
  }

  /**
   * Fetches {@code {baseUrl}/.well-known/openid-configuration} with your HTTP client.
   *
   * @param baseUrl the API's base URL
   * @param httpClient the client
   * @return the document
   * @throws NetworkException when the server can't be reached
   * @throws ApiException when it answers with something other than a discovery document
   */
  public static DiscoveryDocument fetch(String baseUrl, HttpClient httpClient) {
    URI base = TokenVerifier.Builder.checkUri(baseUrl);
    String trimmed = base.toString().replaceAll("/+$", "");
    URI uri = URI.create(trimmed + "/.well-known/openid-configuration");
    HttpResponse<byte[]> response;
    try {
      response =
          httpClient.send(
              HttpRequest.newBuilder(uri)
                  .timeout(TIMEOUT)
                  .header("Accept", "application/json")
                  .GET()
                  .build(),
              HttpResponse.BodyHandlers.ofByteArray());
    } catch (HttpTimeoutException e) {
      throw new TimeoutException(
          "Discovery got no response within " + TIMEOUT.toSeconds() + " s.", e);
    } catch (IOException e) {
      throw new NetworkException("Discovery could not reach " + uri + ".", e);
    } catch (InterruptedException e) {
      throw NetworkException.interrupted(e);
    }
    String requestId = response.headers().firstValue("x-request-id").orElse(null);
    if (response.statusCode() == 200) {
      try {
        DiscoveryDocument document = Json.read(response.body(), Json.type(DiscoveryDocument.class));
        if (document.issuer() != null && document.jwksUri() != null) {
          return document;
        }
      } catch (UncheckedIOException e) {
        // reported below
      }
    }
    String code =
        response.statusCode() == 200 ? "invalid_response" : "http_" + response.statusCode();
    throw new ApiException(
        new ApiError(
            response.statusCode(),
            code,
            "The discovery document could not be read.",
            requestId,
            null,
            List.of(),
            Map.of(),
            false,
            null,
            response.headers().map()));
  }
}
