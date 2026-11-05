package com.qeetgroup.qeetid.testing;

import com.qeetgroup.qeetid.QeetID;
import java.time.Duration;

/** Clients pointed at a {@link MockServer}, with retries that don't wait. */
public final class TestClients {
  /** The API key the test clients send. */
  public static final String API_KEY = "qk_test_0123456789abcdef";

  private TestClients() {}

  /** A builder pointed at {@code server}, with an API key and a 1 ms backoff. */
  public static QeetID.Builder builder(MockServer server) {
    return QeetID.builder()
        .baseUrl(server.url())
        .apiKey(API_KEY)
        .retryBackoff(Duration.ofMillis(1), Duration.ofMillis(2))
        .timeout(Duration.ofSeconds(5));
  }

  /** A client pointed at {@code server}. */
  public static QeetID client(MockServer server) {
    return builder(server).build();
  }
}
