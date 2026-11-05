package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.internal.Transport;

/**
 * Data-subject requests: erasure and export.
 *
 * <p>Get it from {@code qeetId.gdpr()}. Safe for concurrent use.
 */
public final class Gdpr {
  private final GdprErasures erasures;
  private final GdprExports exports;

  /**
   * Used by the client; get this from {@code qeetId.gdpr()}.
   *
   * @param http the client's transport
   */
  public Gdpr(Transport http) {
    this.erasures = new GdprErasures(http);
    this.exports = new GdprExports(http);
  }

  /**
   * Erasure requests (the right to be forgotten).
   *
   * @return the erasures
   */
  public GdprErasures erasures() {
    return erasures;
  }

  /**
   * Data export requests (the right of access).
   *
   * @return the exports
   */
  public GdprExports exports() {
    return exports;
  }
}
