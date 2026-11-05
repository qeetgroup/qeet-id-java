package com.qeetgroup.qeetid.internal;

import com.qeetgroup.qeetid.testing.MockServer;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;

/** Transports pointed at a {@link MockServer}. */
final class Transports {
  static final String API_KEY = "qk_test_0123456789abcdef";

  private Transports() {}

  static Transport apiKey(MockServer server) {
    return create(server, API_KEY, null, null, Duration.ofSeconds(5), 2, System.getLogger("test"));
  }

  static Transport create(
      MockServer server,
      String apiKey,
      String clientId,
      String clientSecret,
      Duration timeout,
      int maxRetries,
      System.Logger logger) {
    return new Transport(
        new Transport.Config(
            server.url(),
            apiKey,
            clientId,
            clientSecret,
            timeout,
            maxRetries,
            Duration.ofMillis(1),
            Duration.ofMillis(2),
            Map.of("X-Trace", "abc"),
            "test-agent",
            logger,
            HttpClient.newHttpClient()));
  }
}
