package com.qeetgroup.qeetid.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qeetgroup.qeetid.ListResult;
import com.qeetgroup.qeetid.Page;
import com.qeetgroup.qeetid.RequestOptions;
import com.qeetgroup.qeetid.exception.ApiException;
import com.qeetgroup.qeetid.exception.ConflictException;
import com.qeetgroup.qeetid.exception.InternalServerException;
import com.qeetgroup.qeetid.exception.NetworkException;
import com.qeetgroup.qeetid.exception.OAuthException;
import com.qeetgroup.qeetid.exception.QeetIDException;
import com.qeetgroup.qeetid.exception.RateLimitException;
import com.qeetgroup.qeetid.exception.SessionRevokedException;
import com.qeetgroup.qeetid.exception.TimeoutException;
import com.qeetgroup.qeetid.testing.MockServer;
import com.qeetgroup.qeetid.testing.MockServer.Reply;
import java.io.IOException;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TransportTest {
  private final MockServer server = new MockServer();
  private final Transport transport = Transports.apiKey(server);

  record Thing(String id) {}

  @AfterEach
  void stop() {
    server.close();
  }

  @Test
  void sendsTheCredentialAcceptUserAgentAndClientHeaders() {
    server.enqueue(200, "{\"id\":\"t1\"}");
    Thing thing = transport.get("/v1/things/t1", null, Thing.class, RequestOptions.none());
    assertEquals("t1", thing.id());
    MockServer.Recorded request = server.onlyRequest();
    assertEquals("GET", request.method());
    assertEquals("ApiKey " + Transports.API_KEY, request.header("authorization"));
    assertEquals("application/json", request.header("accept"));
    assertEquals("test-agent", request.header("user-agent"));
    assertEquals("abc", request.header("x-trace"));
    assertNull(request.header("idempotency-key"));
  }

  @Test
  void sendsPerCallHeadersAndJsonBodies() {
    server.enqueue(201, "{\"id\":\"t2\"}");
    RequestOptions options = RequestOptions.builder().header("traceparent", "00-1-2-01").build();
    transport.send("POST", "/v1/things", Map.of("name", "a b"), Thing.class, options);
    MockServer.Recorded request = server.onlyRequest();
    assertEquals("00-1-2-01", request.header("traceparent"));
    assertEquals("application/json", request.header("content-type"));
    assertEquals("{\"name\":\"a b\"}", request.bodyText());
  }

  @Test
  void encodesQueryValuesAndFormBodies() {
    server.enqueue(200, "{\"id\":\"x\"}");
    ApiRequest request =
        ApiRequest.post("/v1/forms")
            .query("q", "a&b c")
            .query("tag", List.of("x", "y"))
            .query("skipped", null)
            .form(new java.util.LinkedHashMap<>(Map.of("token", "p+q/=")));
    transport.execute(request, RequestOptions.none(), Thing.class);
    MockServer.Recorded recorded = server.onlyRequest();
    assertEquals(List.of("a&b c"), recorded.query().get("q"));
    assertEquals(List.of("x", "y"), recorded.query().get("tag"));
    assertFalse(recorded.query().containsKey("skipped"));
    assertEquals("application/x-www-form-urlencoded", recorded.header("content-type"));
    assertEquals(List.of("p+q/="), recorded.form().get("token"));
  }

  @Test
  void unauthenticatedRequestsSendNoCredential() {
    server.enqueue(200, "{\"id\":\"x\"}");
    transport.execute(
        ApiRequest.post("/v1/agents/token").unauthenticated(), RequestOptions.none(), Thing.class);
    assertNull(server.onlyRequest().header("authorization"));
  }

  @Test
  void aCallNeedingCredentialsFailsWithoutSendingWhenThereAreNone() {
    Transport bare =
        Transports.create(
            server, null, null, null, Duration.ofSeconds(5), 2, System.getLogger("t"));
    QeetIDException error =
        assertThrows(
            QeetIDException.class,
            () -> bare.get("/v1/x", null, Thing.class, RequestOptions.none()));
    assertEquals("missing_credentials", error.code());
    assertTrue(server.requests().isEmpty());
  }

  // Retries

  @ParameterizedTest
  @ValueSource(ints = {408, 429, 500, 502, 503, 504})
  void retriesAGetAfterATransientStatus(int status) {
    server
        .enqueue(status, "{\"error\":{\"code\":\"x\",\"message\":\"m\"}}")
        .enqueue(200, "{\"id\":\"ok\"}");
    assertEquals(
        "ok", transport.get("/v1/things/ok", null, Thing.class, RequestOptions.none()).id());
    assertEquals(2, server.requests().size());
  }

  @Test
  void retriesADeleteButGivesUpAfterMaxRetries() {
    for (int i = 0; i < 3; i++) {
      server.enqueue(500, "{\"error\":{\"code\":\"internal\",\"message\":\"m\"}}");
    }
    assertThrows(
        InternalServerException.class,
        () -> transport.delete("/v1/things/x", RequestOptions.none()));
    assertEquals(3, server.requests().size());
  }

  @ParameterizedTest
  @ValueSource(strings = {"POST", "PUT", "PATCH"})
  void neverRetriesAWriteWithoutAnIdempotencyKey(String method) {
    server.enqueue(503, "{\"error\":{\"code\":\"server.overloaded\",\"message\":\"m\"}}");
    assertThrows(
        ApiException.class,
        () -> transport.send(method, "/v1/things", Map.of(), Thing.class, RequestOptions.none()));
    assertEquals(1, server.requests().size());
  }

  @Test
  void retriesAPostMarkedRetrySafe() {
    server.enqueue(502, "").enqueue(200, "{\"id\":\"checked\"}");
    Thing thing =
        transport.execute(
            ApiRequest.post("/v1/check").retrySafe(), RequestOptions.none(), Thing.class);
    assertEquals("checked", thing.id());
    assertEquals(2, server.requests().size());
  }

  @Test
  void idempotentCreatesSendAGeneratedKeyKeptAcrossRetries() {
    server.enqueue(500, "").enqueue(201, "{\"id\":\"made\"}");
    transport.execute(
        ApiRequest.post("/v1/users").json(Map.of()).idempotent(),
        RequestOptions.none(),
        Thing.class);
    List<MockServer.Recorded> requests = server.requests();
    assertEquals(2, requests.size());
    String key = requests.get(0).header("idempotency-key");
    UUID.fromString(key);
    assertEquals(key, requests.get(1).header("idempotency-key"));
  }

  @Test
  void eachIdempotentCallGetsItsOwnKey() {
    server.enqueue(201, "{\"id\":\"a\"}").enqueue(201, "{\"id\":\"b\"}");
    transport.execute(
        ApiRequest.post("/v1/users").idempotent(), RequestOptions.none(), Thing.class);
    transport.execute(
        ApiRequest.post("/v1/users").idempotent(), RequestOptions.none(), Thing.class);
    assertNotEquals(
        server.requests().get(0).header("idempotency-key"),
        server.requests().get(1).header("idempotency-key"));
  }

  @Test
  void theCallersIdempotencyKeyIsSent() {
    server.enqueue(201, "{\"id\":\"a\"}");
    RequestOptions options = RequestOptions.builder().idempotencyKey("onboarding-42").build();
    transport.execute(ApiRequest.post("/v1/users").idempotent(), options, Thing.class);
    assertEquals("onboarding-42", server.onlyRequest().header("idempotency-key"));
  }

  @Test
  void anIdempotencyKeyOnACallTheApiDoesNotMakeIdempotentIsRefused() {
    RequestOptions options = RequestOptions.builder().idempotencyKey("k").build();
    assertThrows(
        IllegalArgumentException.class,
        () -> transport.send("POST", "/v1/roles", Map.of(), Thing.class, options));
    assertTrue(server.requests().isEmpty());
  }

  @Test
  void retriesAnIdempotencyKeyStillInProgress() {
    server
        .enqueue(409, "{\"error\":{\"code\":\"idempotency.in_progress\",\"message\":\"m\"}}")
        .enqueue(201, "{\"id\":\"done\"}");
    Thing thing =
        transport.execute(
            ApiRequest.post("/v1/users").idempotent(), RequestOptions.none(), Thing.class);
    assertEquals("done", thing.id());
  }

  @Test
  void doesNotRetryAnOrdinaryConflict() {
    server.enqueue(409, "{\"error\":{\"code\":\"idempotency.outcome_unknown\",\"message\":\"m\"}}");
    ConflictException error =
        assertThrows(
            ConflictException.class,
            () ->
                transport.execute(
                    ApiRequest.post("/v1/users").idempotent(), RequestOptions.none(), Thing.class));
    assertEquals("idempotency.outcome_unknown", error.code());
    assertEquals(1, server.requests().size());
  }

  @Test
  void neverRetriesARevokedSessionEvenOnAGet() {
    server.enqueue(
        401,
        "{\"error\":{\"code\":\"auth.session_revoked\",\"message\":\"m\",\"retryable\":true}}");
    SessionRevokedException error =
        assertThrows(
            SessionRevokedException.class,
            () -> transport.get("/v1/x", null, Thing.class, RequestOptions.none()));
    assertTrue(error.retryable());
    assertEquals(1, server.requests().size());
  }

  @Test
  void neverRetriesARequestMarkedNeverRetry() {
    server.enqueue(503, "");
    assertThrows(
        ApiException.class,
        () ->
            transport.execute(
                ApiRequest.get("/v1/x").neverRetry(), RequestOptions.none(), Thing.class));
    assertEquals(1, server.requests().size());
  }

  @Test
  void perCallMaxRetriesOverridesTheClient() {
    server.enqueue(500, "").enqueue(200, "{\"id\":\"x\"}");
    RequestOptions noRetries = RequestOptions.builder().maxRetries(0).build();
    assertThrows(
        InternalServerException.class, () -> transport.get("/v1/x", null, Thing.class, noRetries));
    assertEquals(1, server.requests().size());
  }

  @Test
  void honoursRetryAfter() {
    server
        .enqueue(
            Reply.json(429, "{\"error\":{\"code\":\"too_many_requests\",\"message\":\"m\"}}")
                .withHeader("Retry-After", "1"))
        .enqueue(200, "{\"id\":\"x\"}");
    long started = System.nanoTime();
    transport.get("/v1/x", null, Thing.class, RequestOptions.none());
    assertTrue(Duration.ofNanos(System.nanoTime() - started).toMillis() >= 900);
  }

  @Test
  void throwsInsteadOfWaitingOutALongRetryAfter() {
    server.enqueue(
        Reply.json(429, "{\"error\":{\"code\":\"too_many_requests\",\"message\":\"m\"}}")
            .withHeader("Retry-After", "120"));
    RateLimitException error =
        assertThrows(
            RateLimitException.class,
            () -> transport.get("/v1/x", null, Thing.class, RequestOptions.none()));
    assertEquals(Duration.ofMinutes(2), error.retryAfter());
    assertEquals(1, server.requests().size());
  }

  // Failures without an answer

  @Test
  void aSlowAnswerIsATimeoutAndIsRetriedForAGet() {
    server
        .enqueue(Reply.json(200, "{\"id\":\"slow\"}").withDelay(Duration.ofMillis(800)))
        .enqueue(200, "{\"id\":\"fast\"}");
    RequestOptions options = RequestOptions.builder().timeout(Duration.ofMillis(200)).build();
    assertEquals("fast", transport.get("/v1/x", null, Thing.class, options).id());
  }

  @Test
  void aTimeoutIsThrownWhenRetriesRunOut() {
    server.enqueue(Reply.json(200, "{}").withDelay(Duration.ofMillis(800)));
    RequestOptions options =
        RequestOptions.builder().timeout(Duration.ofMillis(150)).maxRetries(0).build();
    TimeoutException error =
        assertThrows(
            TimeoutException.class, () -> transport.get("/v1/x", null, Thing.class, options));
    assertEquals("timeout", error.code());
  }

  @Test
  void noServerIsANetworkErrorRetriedThenThrown() throws IOException {
    int port;
    try (ServerSocket socket = new ServerSocket(0)) {
      port = socket.getLocalPort();
    }
    Transport closed =
        new Transport(
            new Transport.Config(
                "http://127.0.0.1:" + port,
                Transports.API_KEY,
                null,
                null,
                Duration.ofSeconds(2),
                1,
                Duration.ofMillis(1),
                Duration.ofMillis(1),
                Map.of(),
                "ua",
                System.getLogger("t"),
                java.net.http.HttpClient.newHttpClient()));
    NetworkException error =
        assertThrows(
            NetworkException.class,
            () -> closed.get("/v1/x", null, Thing.class, RequestOptions.none()));
    assertEquals("network_error", error.code());
    assertFalse(error.getMessage().contains(Transports.API_KEY));
  }

  @Test
  void aDroppedConnectionIsANetworkError() {
    server.enqueue(Reply.drop()).enqueue(200, "{\"id\":\"again\"}");
    assertEquals("again", transport.get("/v1/x", null, Thing.class, RequestOptions.none()).id());
  }

  @Test
  void anInterruptedCallStopsAndKeepsTheInterrupt() {
    server.enqueue(Reply.json(200, "{}").withDelay(Duration.ofSeconds(2)));
    Thread.currentThread().interrupt();
    try {
      NetworkException error =
          assertThrows(
              NetworkException.class,
              () -> transport.get("/v1/x", null, Thing.class, RequestOptions.none()));
      assertEquals("interrupted", error.code());
      assertTrue(Thread.currentThread().isInterrupted());
    } finally {
      Thread.interrupted();
    }
  }

  // Answers

  @Test
  void aSuccessfulAnswerThatIsNotJsonIsAnInvalidResponse() {
    server.enqueue(Reply.json(200, "<html>").withHeader("X-Request-Id", "req_1"));
    ApiException error =
        assertThrows(
            ApiException.class,
            () -> transport.get("/v1/x", null, Thing.class, RequestOptions.none()));
    assertEquals("invalid_response", error.code());
    assertEquals(200, error.status());
    assertEquals("req_1", error.requestId());
  }

  @Test
  void anEmptyAnswerWhereABodyIsExpectedIsAnInvalidResponse() {
    server.enqueue(Reply.empty(200));
    ApiException error =
        assertThrows(
            ApiException.class,
            () -> transport.get("/v1/x", null, Thing.class, RequestOptions.none()));
    assertEquals("invalid_response", error.code());
  }

  @Test
  void aVoidCallIgnoresTheBody() {
    server.enqueue(Reply.empty(204));
    transport.delete("/v1/x", RequestOptions.none());
    assertEquals("DELETE", server.onlyRequest().method());
  }

  @Test
  void anOAuthErrorIsTyped() {
    server.enqueue(401, "{\"error\":\"invalid_client\",\"error_description\":\"Unknown client.\"}");
    OAuthException error =
        assertThrows(
            OAuthException.class,
            () ->
                transport.execute(
                    ApiRequest.post("/v1/oauth/token").unauthenticated().neverRetry(),
                    RequestOptions.none(),
                    Thing.class));
    assertEquals("invalid_client", error.error());
    assertEquals(401, error.status());
    assertEquals("Unknown client.", error.errorDescription());
  }

  // Lists

  @Test
  void readsAWholeList() {
    server.enqueue(200, "{\"items\":[{\"id\":\"a\"},{\"id\":\"b\"}],\"truncated\":true}");
    ListResult<Thing> list = transport.list("/v1/things", null, Thing.class, RequestOptions.none());
    assertEquals(List.of(new Thing("a"), new Thing("b")), list.data());
    assertTrue(list.truncated());
  }

  @Test
  void aWholeListWithoutItemsIsEmpty() {
    server.enqueue(200, "{}");
    ListResult<Thing> list = transport.list("/v1/things", null, Thing.class, RequestOptions.none());
    assertTrue(list.data().isEmpty());
    assertFalse(list.truncated());
  }

  @Test
  void walksEveryPageLazily() {
    server
        .enqueue(200, "{\"items\":[{\"id\":\"1\"},{\"id\":\"2\"}],\"next_cursor\":\"c2\"}")
        .enqueue(200, "{\"items\":[{\"id\":\"3\"}],\"next_cursor\":\"c3\"}")
        .enqueue(200, "{\"items\":[{\"id\":\"4\"}],\"next_cursor\":\"\"}");
    Page<Thing> page = transport.page("/v1/things", null, Thing.class, RequestOptions.none());
    assertEquals(1, server.requests().size(), "only the first page is fetched up front");
    assertEquals(List.of("1", "2"), page.data().stream().map(Thing::id).toList());
    assertTrue(page.hasNextPage());

    List<String> all = new ArrayList<>();
    for (Thing thing : page) {
      all.add(thing.id());
    }
    assertEquals(List.of("1", "2", "3", "4"), all);
    List<MockServer.Recorded> requests = server.requests();
    assertEquals(3, requests.size());
    assertNull(requests.get(0).query().get("cursor"));
    assertEquals(List.of("c2"), requests.get(1).query().get("cursor"));
    assertEquals(List.of("c3"), requests.get(2).query().get("cursor"));
  }

  @Test
  void streamsPagesAndStopsWhenTheStreamDoes() {
    server
        .enqueue(200, "{\"items\":[{\"id\":\"1\"}],\"next_cursor\":\"c2\"}")
        .enqueue(200, "{\"items\":[{\"id\":\"2\"}],\"next_cursor\":\"c3\"}");
    Page<Thing> page = transport.page("/v1/things", null, Thing.class, RequestOptions.none());
    assertEquals(
        List.of("1", "2"), page.stream().limit(2).map(Thing::id).collect(Collectors.toList()));
    assertEquals(2, server.requests().size());
  }

  @Test
  void stopsWhenTheServerRepeatsACursor() {
    server
        .enqueue(200, "{\"items\":[{\"id\":\"1\"}],\"next_cursor\":\"same\"}")
        .enqueue(200, "{\"items\":[{\"id\":\"2\"}],\"next_cursor\":\"same\"}");
    Page<Thing> page = transport.page("/v1/things", null, Thing.class, RequestOptions.none());
    List<Page<Thing>> pages = new ArrayList<>();
    page.pages().forEach(pages::add);
    assertEquals(2, pages.size());
    assertEquals(2, server.requests().size());
  }

  @Test
  void theLastPageHasNoNextPage() {
    server.enqueue(200, "{\"items\":[]}");
    Page<Thing> page = transport.page("/v1/things", null, Thing.class, RequestOptions.none());
    assertFalse(page.hasNextPage());
    assertNull(page.nextCursor());
    assertThrows(NoSuchElementException.class, page::nextPage);
    assertFalse(page.iterator().hasNext());
  }

  // Client credentials

  @Test
  void clientCredentialsAreExchangedOnceAndTheTokenReused() {
    Transport cc =
        Transports.create(
            server, null, "svc_1", "cs_secret", Duration.ofSeconds(5), 2, System.getLogger("t"));
    server
        .enqueue(200, "{\"access_token\":\"tok1\",\"token_type\":\"Bearer\",\"expires_in\":3600}")
        .enqueue(200, "{\"id\":\"a\"}")
        .enqueue(200, "{\"id\":\"b\"}");
    cc.get("/v1/a", null, Thing.class, RequestOptions.none());
    cc.get("/v1/b", null, Thing.class, RequestOptions.none());
    List<MockServer.Recorded> requests = server.requests();
    assertEquals(3, requests.size());
    MockServer.Recorded token = requests.get(0);
    assertEquals("/v1/oauth/token", token.path());
    assertNull(token.header("authorization"));
    assertEquals(
        Map.of(
            "grant_type",
            List.of("client_credentials"),
            "client_id",
            List.of("svc_1"),
            "client_secret",
            List.of("cs_secret")),
        token.form());
    assertEquals("Bearer tok1", requests.get(1).header("authorization"));
    assertEquals("Bearer tok1", requests.get(2).header("authorization"));
  }

  @Test
  void aRefusedTokenIsForgottenSoTheNextCallGetsANewOne() {
    Transport cc =
        Transports.create(
            server, null, "svc_1", "cs_secret", Duration.ofSeconds(5), 2, System.getLogger("t"));
    server
        .enqueue(200, "{\"access_token\":\"tok1\",\"expires_in\":3600}")
        .enqueue(401, "{\"error\":{\"code\":\"unauthorized\",\"message\":\"m\"}}")
        .enqueue(200, "{\"access_token\":\"tok2\",\"expires_in\":3600}")
        .enqueue(200, "{\"id\":\"b\"}");
    assertThrows(
        ApiException.class, () -> cc.get("/v1/a", null, Thing.class, RequestOptions.none()));
    cc.get("/v1/b", null, Thing.class, RequestOptions.none());
    assertEquals("Bearer tok2", server.lastRequest().header("authorization"));
  }

  @Test
  void aShortLivedTokenIsReplacedBeforeItExpires() {
    Transport cc =
        Transports.create(
            server, null, "svc_1", "cs_secret", Duration.ofSeconds(5), 2, System.getLogger("t"));
    server
        .enqueue(200, "{\"access_token\":\"tok1\",\"expires_in\":0}")
        .enqueue(200, "{\"id\":\"a\"}")
        .enqueue(200, "{\"access_token\":\"tok2\",\"expires_in\":3600}")
        .enqueue(200, "{\"id\":\"b\"}");
    cc.get("/v1/a", null, Thing.class, RequestOptions.none());
    cc.get("/v1/b", null, Thing.class, RequestOptions.none());
    assertEquals("Bearer tok2", server.lastRequest().header("authorization"));
  }

  @Test
  void refusedClientCredentialsAreAnOAuthErrorAndAreNotRetried() {
    Transport cc =
        Transports.create(
            server, null, "svc_1", "wrong", Duration.ofSeconds(5), 2, System.getLogger("t"));
    server.enqueue(401, "{\"error\":\"invalid_client\",\"error_description\":\"Unknown client.\"}");
    OAuthException error =
        assertThrows(
            OAuthException.class, () -> cc.get("/v1/a", null, Thing.class, RequestOptions.none()));
    assertEquals(OAuthException.INVALID_CLIENT, error.code());
    assertEquals(1, server.requests().size());
    assertFalse(error.getMessage().contains("wrong"));
  }

  // Logging

  @Test
  void logsMethodPathStatusAndRequestIdButNeverCredentialsOrQueries() {
    List<String> lines = new ArrayList<>();
    System.Logger capture = new CapturingLogger(lines);
    Transport logged =
        Transports.create(
            server, Transports.API_KEY, null, null, Duration.ofSeconds(5), 2, capture);
    server
        .enqueue(Reply.json(500, "{}").withHeader("X-Request-Id", "req_a"))
        .enqueue(Reply.json(200, "{\"id\":\"x\"}").withHeader("X-Request-Id", "req_b"));
    logged.execute(
        ApiRequest.get("/v1/users").query("q", "secret-filter"),
        RequestOptions.none(),
        Thing.class);
    String all = String.join("\n", lines);
    assertTrue(all.contains("GET /v1/users attempt 1 answered 500"), all);
    assertTrue(all.contains("retrying GET /v1/users"), all);
    assertTrue(all.contains("req_b"), all);
    assertFalse(all.contains(Transports.API_KEY), all);
    assertFalse(all.contains("secret-filter"), all);
  }

  /** A System.Logger that records formatted DEBUG lines. */
  private record CapturingLogger(List<String> lines) implements System.Logger {
    @Override
    public String getName() {
      return "capture";
    }

    @Override
    public boolean isLoggable(Level level) {
      return true;
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String message, Throwable thrown) {
      lines.add(message);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
      lines.add(java.text.MessageFormat.format(format, params));
    }

    @Override
    public void log(Level level, Supplier<String> message) {
      lines.add(message.get());
    }
  }

  @Test
  void anErrorIsAQeetIDException() {
    server.enqueue(500, "{}");
    QeetIDException error =
        assertThrows(
            QeetIDException.class,
            () -> transport.send("POST", "/v1/x", null, Thing.class, RequestOptions.none()));
    assertInstanceOf(InternalServerException.class, error);
  }
}
