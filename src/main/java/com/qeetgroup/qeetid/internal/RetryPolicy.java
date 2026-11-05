package com.qeetgroup.qeetid.internal;

import com.qeetgroup.qeetid.exception.ApiException;
import com.qeetgroup.qeetid.exception.NetworkException;
import com.qeetgroup.qeetid.exception.QeetIDException;
import java.time.Duration;
import java.util.function.DoubleSupplier;
import org.jspecify.annotations.Nullable;

/**
 * When a failed attempt is tried again, and after how long. Only a retry-safe request is retried
 * (GET, DELETE, a POST that changes nothing, or a request carrying an {@code Idempotency-Key}), and
 * only after a failure a later attempt can fix: no response, a timeout, 408, 429, 500, 502, 503,
 * 504, or a 409 saying the first request with the same idempotency key is still running. A 401
 * {@code auth.session_revoked} is never retried, whatever the server's {@code retryable} hint says:
 * the session has ended and only a new sign-in helps.
 */
public final class RetryPolicy {
  /** A {@code Retry-After} longer than this is not waited out: the error is thrown instead. */
  public static final Duration MAX_RETRY_AFTER = Duration.ofSeconds(60);

  private static final String IDEMPOTENCY_IN_PROGRESS = "idempotency.in_progress";

  private RetryPolicy() {}

  /** Whether {@code failure} may be retried, given whether the request is safe to repeat. */
  public static boolean isRetryable(QeetIDException failure, boolean retrySafe) {
    if (!retrySafe) {
      return false;
    }
    if (failure instanceof NetworkException) {
      return !"interrupted".equals(failure.code()); // TimeoutException included
    }
    if (!(failure instanceof ApiException api)) {
      return false;
    }
    if (ErrorMapper.SESSION_REVOKED.equals(api.code())) {
      return false;
    }
    return switch (api.status()) {
      case 408, 429, 500, 502, 503, 504 -> true;
      case 409 -> IDEMPOTENCY_IN_PROGRESS.equals(api.code());
      default -> false;
    };
  }

  /**
   * How long to wait before retry number {@code retry} (0 for the first). The server's {@code
   * Retry-After} when it sent one; otherwise exponential backoff with "equal jitter": half the
   * capped delay plus a random share of the other half, so clients that failed together don't retry
   * together. Null when the server asks for a wait longer than {@link #MAX_RETRY_AFTER}, meaning:
   * don't retry, throw.
   */
  public static @Nullable Duration delay(
      int retry, QeetIDException failure, Duration initial, Duration max, DoubleSupplier random) {
    if (failure instanceof ApiException api && api.retryAfter() != null) {
      Duration asked = api.retryAfter();
      return asked.compareTo(MAX_RETRY_AFTER) > 0 ? null : asked;
    }
    long cap = max.toMillis();
    long exponential = retry >= 30 ? cap : Math.min(cap, initial.toMillis() << retry);
    double half = exponential / 2.0;
    return Duration.ofMillis(Math.round(half + random.getAsDouble() * half));
  }
}
