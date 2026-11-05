package com.qeetgroup.qeetid.federation;

/**
 * A new link. {@code token} and {@code url} are shown only here.
 *
 * @param link the link
 * @param token the token
 * @param url the URL
 */
public record AdminPortalLinkCreated(AdminPortalLink link, String token, String url) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "AdminPortalLinkCreated[link=" + link + ", token=[redacted], url=[redacted]]";
  }
}
