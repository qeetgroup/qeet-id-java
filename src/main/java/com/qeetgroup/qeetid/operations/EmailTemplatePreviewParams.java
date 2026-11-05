package com.qeetgroup.qeetid.operations;

import com.qeetgroup.qeetid.Params;
import java.util.Map;

/** What to render a template with. */
public final class EmailTemplatePreviewParams extends Params {
  private EmailTemplatePreviewParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link EmailTemplatePreviewParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link EmailTemplatePreviewParams}. */
  public static final class Builder extends Params.Builder<EmailTemplatePreviewParams, Builder> {
    private Builder() {}

    /**
     * The vars.
     *
     * @param vars the value
     * @return this builder
     */
    public Builder vars(Map<String, String> vars) {
      return set("vars", nonNull(vars, "vars"));
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

    /**
     * Preview unsaved content instead of the saved template.
     *
     * @param draft the value
     * @return this builder
     */
    public Builder draft(EmailTemplateContent draft) {
      return set("draft", nonNull(draft, "draft"));
    }

    @Override
    public EmailTemplatePreviewParams build() {
      return new EmailTemplatePreviewParams(this);
    }
  }
}
