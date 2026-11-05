package com.qeetgroup.qeetid.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qeetgroup.qeetid.exception.ApiError;
import com.qeetgroup.qeetid.exception.ApiException;
import com.qeetgroup.qeetid.exception.NetworkException;
import com.qeetgroup.qeetid.exception.QeetIDException;
import com.qeetgroup.qeetid.exception.TimeoutException;
import com.qeetgroup.qeetid.exception.WebhookVerificationException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RetryPolicyTest {
  private static final Duration INITIAL = Duration.ofMillis(500);
  private static final Duration MAX = Duration.ofSeconds(8);

  private static ApiException api(int status, String code, Duration retryAfter) {
    return new ApiException(
        new ApiError(
            status, code, "m", null, null, List.of(), Map.of(), false, retryAfter, Map.of()));
  }

  @Test
  void onlyRetrySafeRequestsAreRetried() {
    assertFalse(RetryPolicy.isRetryable(api(503, "x", null), false));
    assertTrue(RetryPolicy.isRetryable(api(503, "x", null), true));
  }

  @Test
  void retriesTransientFailuresOnly() {
    for (int status : new int[] {408, 429, 500, 502, 503, 504}) {
      assertTrue(RetryPolicy.isRetryable(api(status, "x", null), true), "status " + status);
    }
    for (int status : new int[] {400, 401, 403, 404, 405, 409, 422, 501}) {
      assertFalse(RetryPolicy.isRetryable(api(status, "x", null), true), "status " + status);
    }
    assertTrue(RetryPolicy.isRetryable(api(409, "idempotency.in_progress", null), true));
    assertTrue(RetryPolicy.isRetryable(new NetworkException("down", null), true));
    assertTrue(RetryPolicy.isRetryable(new TimeoutException("slow", null), true));
    assertFalse(
        RetryPolicy.isRetryable(NetworkException.interrupted(new InterruptedException()), true));
    Thread.interrupted();
    assertFalse(
        RetryPolicy.isRetryable(
            new WebhookVerificationException(
                WebhookVerificationException.Reason.SIGNATURE_MISMATCH, "m"),
            true));
    assertFalse(RetryPolicy.isRetryable(new QeetIDException("m", "missing_credentials"), true));
  }

  @Test
  void neverRetriesARevokedSessionWhateverItsStatus() {
    assertFalse(RetryPolicy.isRetryable(api(401, "auth.session_revoked", null), true));
    assertFalse(RetryPolicy.isRetryable(api(503, "auth.session_revoked", null), true));
  }

  @Test
  void backsOffExponentiallyWithEqualJitterUpToTheCap() {
    QeetIDException failure = api(500, "x", null);
    assertEquals(Duration.ofMillis(250), RetryPolicy.delay(0, failure, INITIAL, MAX, () -> 0.0));
    assertEquals(Duration.ofMillis(500), RetryPolicy.delay(0, failure, INITIAL, MAX, () -> 1.0));
    assertEquals(Duration.ofMillis(750), RetryPolicy.delay(1, failure, INITIAL, MAX, () -> 0.5));
    assertEquals(Duration.ofSeconds(4), RetryPolicy.delay(10, failure, INITIAL, MAX, () -> 0.0));
    assertEquals(Duration.ofSeconds(8), RetryPolicy.delay(40, failure, INITIAL, MAX, () -> 1.0));
  }

  @Test
  void honoursRetryAfterUpToAMinute() {
    assertEquals(
        Duration.ofSeconds(2),
        RetryPolicy.delay(0, api(429, "x", Duration.ofSeconds(2)), INITIAL, MAX, () -> 0.5));
    assertEquals(
        Duration.ofSeconds(60),
        RetryPolicy.delay(0, api(429, "x", Duration.ofSeconds(60)), INITIAL, MAX, () -> 0.5));
    assertNull(
        RetryPolicy.delay(0, api(429, "x", Duration.ofSeconds(61)), INITIAL, MAX, () -> 0.5));
  }
}
