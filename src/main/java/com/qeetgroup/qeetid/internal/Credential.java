package com.qeetgroup.qeetid.internal;

import com.qeetgroup.qeetid.RequestOptions;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import org.jspecify.annotations.Nullable;

/** How a client authenticates its calls. Never prints the secret it holds. */
abstract class Credential {
  /** The {@code Authorization} header value for the next request. */
  abstract String authorization();

  /** Forgets a cached token the API refused, so the next call gets a new one. */
  void invalidate(String presented) {}

  /** An API key, sent as {@code Authorization: ApiKey <key>}. */
  static final class ApiKey extends Credential {
    private final String header;

    ApiKey(String key) {
      this.header = "ApiKey " + key;
    }

    @Override
    String authorization() {
      return header;
    }

    @Override
    public String toString() {
      return "ApiKey[redacted]";
    }
  }

  /**
   * A service principal's client ID and secret, exchanged for an access token with the {@code
   * client_credentials} grant ({@code POST /v1/oauth/token}). The token is cached and replaced
   * shortly before it expires; concurrent callers share one fetch.
   */
  static final class ClientCredentials extends Credential {
    /** How long before expiry a token is replaced, at most. */
    private static final Duration REFRESH_MARGIN = Duration.ofSeconds(60);

    private final String clientId;
    private final String clientSecret;
    private final Transport transport;
    private final Clock clock;
    private final ReentrantLock lock = new ReentrantLock();
    private @Nullable String header;
    private Instant refreshAt = Instant.MIN;

    ClientCredentials(String clientId, String clientSecret, Transport transport, Clock clock) {
      this.clientId = clientId;
      this.clientSecret = clientSecret;
      this.transport = transport;
      this.clock = clock;
    }

    @Override
    String authorization() {
      lock.lock();
      try {
        String current = header;
        if (current != null && clock.instant().isBefore(refreshAt)) {
          return current;
        }
        Map<String, @Nullable String> form = new LinkedHashMap<>();
        form.put("grant_type", "client_credentials");
        form.put("client_id", clientId);
        form.put("client_secret", clientSecret);
        ApiRequest request =
            ApiRequest.post("/v1/oauth/token").form(form).unauthenticated().neverRetry();
        Instant asked = clock.instant();
        Envelopes.TokenAnswer answer =
            transport.execute(request, RequestOptions.none(), Envelopes.TokenAnswer.class);
        Duration lifetime = Duration.ofSeconds(Math.max(answer.expiresIn(), 0));
        Duration margin =
            lifetime.dividedBy(2).compareTo(REFRESH_MARGIN) < 0
                ? lifetime.dividedBy(2)
                : REFRESH_MARGIN;
        String fresh = "Bearer " + answer.accessToken();
        header = fresh;
        refreshAt = asked.plus(lifetime).minus(margin);
        return fresh;
      } finally {
        lock.unlock();
      }
    }

    @Override
    void invalidate(String presented) {
      lock.lock();
      try {
        if (presented.equals(header)) {
          header = null;
        }
      } finally {
        lock.unlock();
      }
    }

    @Override
    public String toString() {
      return "ClientCredentials[clientId=" + clientId + ", clientSecret=[redacted]]";
    }
  }
}
