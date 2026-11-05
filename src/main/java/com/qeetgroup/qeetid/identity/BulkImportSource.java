package com.qeetgroup.qeetid.identity;

/** The identity provider a bulk import file comes from, and the file's format. */
public enum BulkImportSource {
  /** An Auth0 user export: NDJSON. */
  AUTH0("auth0", "application/x-ndjson"),
  /** An Amazon Cognito user export: CSV. */
  COGNITO("cognito", "text/csv"),
  /** An Azure AD B2C export: Microsoft Graph JSON. */
  AZURE_B2C("azure_b2c", "application/json");

  private final String wireName;
  private final String contentType;

  BulkImportSource(String wireName, String contentType) {
    this.wireName = wireName;
    this.contentType = contentType;
  }

  /**
   * The name the API knows it by, such as {@code auth0}.
   *
   * @return the name
   */
  public String wireName() {
    return wireName;
  }

  /**
   * The file's content type.
   *
   * @return the content type
   */
  public String contentType() {
    return contentType;
  }
}
