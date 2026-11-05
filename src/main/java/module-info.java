/**
 * The server-side Java SDK for Qeet ID: the management API, webhook and access-token verification,
 * and OAuth. Start with {@link com.qeetgroup.qeetid.QeetID}.
 */
module com.qeetgroup.qeetid {
  requires transitive java.net.http;
  requires static transitive org.jspecify;
  requires com.fasterxml.jackson.databind;

  exports com.qeetgroup.qeetid;
  exports com.qeetgroup.qeetid.exception;

  opens com.qeetgroup.qeetid.internal to
      com.fasterxml.jackson.databind;
}
