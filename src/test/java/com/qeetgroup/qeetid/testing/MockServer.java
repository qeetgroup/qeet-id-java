package com.qeetgroup.qeetid.testing;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**
 * An in-process HTTP server for tests: answers from a queue (or a handler) and records every
 * request it receives. Bound to the loopback address on a free port.
 */
public final class MockServer implements AutoCloseable {
  private final HttpServer server;
  private final BlockingQueue<Reply> replies = new LinkedBlockingQueue<>();
  private final ConcurrentLinkedQueue<Recorded> requests = new ConcurrentLinkedQueue<>();
  private volatile Function<Recorded, Reply> handler;

  public MockServer() {
    try {
      server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    server.setExecutor(Executors.newCachedThreadPool());
    server.createContext("/", this::handle);
    server.start();
  }

  /** The server's base URL, http://127.0.0.1:port. */
  public String url() {
    return "http://127.0.0.1:" + server.getAddress().getPort();
  }

  /** Queues an answer. */
  public MockServer enqueue(Reply reply) {
    replies.add(reply);
    return this;
  }

  /** Queues a JSON answer. */
  public MockServer enqueue(int status, String json) {
    return enqueue(Reply.json(status, json));
  }

  /** Answers every request with the handler, once the queue is empty. */
  public MockServer handler(Function<Recorded, Reply> handler) {
    this.handler = handler;
    return this;
  }

  /** The requests received so far, oldest first. */
  public List<Recorded> requests() {
    return List.copyOf(requests);
  }

  /** The only request received; fails when there were none or several. */
  public Recorded onlyRequest() {
    List<Recorded> all = requests();
    if (all.size() != 1) {
      throw new AssertionError("expected one request, got " + all.size() + ": " + all);
    }
    return all.get(0);
  }

  /** The last request received. */
  public Recorded lastRequest() {
    List<Recorded> all = requests();
    if (all.isEmpty()) {
      throw new AssertionError("no request was received");
    }
    return all.get(all.size() - 1);
  }

  @Override
  public void close() {
    server.stop(0);
  }

  private void handle(HttpExchange exchange) throws IOException {
    byte[] body;
    try (InputStream in = exchange.getRequestBody()) {
      body = in.readAllBytes();
    }
    Map<String, List<String>> headers = new LinkedHashMap<>();
    exchange
        .getRequestHeaders()
        .forEach((name, values) -> headers.put(name.toLowerCase(Locale.ROOT), List.copyOf(values)));
    Recorded request =
        new Recorded(
            exchange.getRequestMethod(),
            exchange.getRequestURI().getRawPath(),
            exchange.getRequestURI().getRawQuery(),
            Collections.unmodifiableMap(headers),
            body);
    requests.add(request);
    Reply reply = replies.poll();
    if (reply == null) {
      Function<Recorded, Reply> fallback = handler;
      reply =
          fallback != null
              ? fallback.apply(request)
              : Reply.json(
                  500, "{\"error\":{\"code\":\"no_reply\",\"message\":\"No reply queued.\"}}");
    }
    if (!reply.delay().isZero()) {
      try {
        Thread.sleep(reply.delay().toMillis());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    if (reply.dropConnection()) {
      exchange.close();
      return;
    }
    reply.headers().forEach((name, value) -> exchange.getResponseHeaders().add(name, value));
    byte[] bytes = reply.body().getBytes(StandardCharsets.UTF_8);
    exchange.sendResponseHeaders(reply.status(), bytes.length == 0 ? -1 : bytes.length);
    try (OutputStream out = exchange.getResponseBody()) {
      out.write(bytes);
    }
  }

  /** An answer. */
  public record Reply(
      int status,
      Map<String, String> headers,
      String body,
      Duration delay,
      boolean dropConnection) {

    public static Reply json(int status, String json) {
      return new Reply(
          status, Map.of("Content-Type", "application/json"), json, Duration.ZERO, false);
    }

    public static Reply empty(int status) {
      return new Reply(status, Map.of(), "", Duration.ZERO, false);
    }

    public static Reply drop() {
      return new Reply(0, Map.of(), "", Duration.ZERO, true);
    }

    public Reply withHeader(String name, String value) {
      Map<String, String> copy = new LinkedHashMap<>(headers);
      copy.put(name, value);
      return new Reply(status, copy, body, delay, dropConnection);
    }

    public Reply withDelay(Duration delay) {
      return new Reply(status, headers, body, delay, dropConnection);
    }
  }

  /** A request the server received. */
  @SuppressWarnings("ArrayRecordComponent") // a test value, never compared
  public record Recorded(
      String method, String path, String rawQuery, Map<String, List<String>> headers, byte[] body) {

    /** A header's first value, or null. */
    public String header(String name) {
      List<String> values = headers.get(name.toLowerCase(Locale.ROOT));
      return values == null || values.isEmpty() ? null : values.get(0);
    }

    /** The body as UTF-8 text. */
    public String bodyText() {
      return new String(body, StandardCharsets.UTF_8);
    }

    /** The query's parameters, decoded, in order; a repeated name has several values. */
    public Map<String, List<String>> query() {
      return decodeForm(rawQuery);
    }

    /** A form body's fields, decoded. */
    public Map<String, List<String>> form() {
      return decodeForm(bodyText());
    }

    private static Map<String, List<String>> decodeForm(String text) {
      Map<String, List<String>> out = new LinkedHashMap<>();
      if (text == null || text.isEmpty()) {
        return out;
      }
      for (String pair : text.split("&", -1)) {
        int eq = pair.indexOf('=');
        String name =
            URLDecoder.decode(eq < 0 ? pair : pair.substring(0, eq), StandardCharsets.UTF_8);
        String value =
            eq < 0 ? "" : URLDecoder.decode(pair.substring(eq + 1), StandardCharsets.UTF_8);
        out.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
      }
      return out;
    }

    @Override
    public String toString() {
      return method + " " + path + (rawQuery == null ? "" : "?" + rawQuery);
    }
  }
}
