package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;

/** A draft to send to the caller's own address. */
public final class EmailTemplateTestParams extends Params {
  private EmailTemplateTestParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link EmailTemplateTestParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link EmailTemplateTestParams}. */
  public static final class Builder extends Params.Builder<EmailTemplateTestParams, Builder> {
    private Builder() {}

    /**
     * The draft. Required.
     *
     * @param draft the value
     * @return this builder
     */
    public Builder draft(EmailTemplateContent draft) {
      return set("draft", nonNull(draft, "draft"));
    }

    /**
     * The locale.
     *
     * @param locale the value
     * @return this builder
     */
    public Builder locale(String locale) {
      return set("locale", nonNull(locale, "locale"));
    }

    @Override
    public EmailTemplateTestParams build() {
      require("draft", "draft");
      return new EmailTemplateTestParams(this);
    }
  }
}
