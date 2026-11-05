package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;
import java.util.List;
import java.util.Map;

/** Several evaluations in one call. Top-level fields are defaults each evaluation may replace. */
public final class EvaluationsParams extends Params {
  private EvaluationsParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link EvaluationsParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link EvaluationsParams}. */
  public static final class Builder extends Params.Builder<EvaluationsParams, Builder> {
    private Builder() {}

    /**
     * The subject.
     *
     * @param subject the value
     * @return this builder
     */
    public Builder subject(AuthzenEntity subject) {
      return set("subject", nonNull(subject, "subject"));
    }

    /**
     * The resource.
     *
     * @param resource the value
     * @return this builder
     */
    public Builder resource(AuthzenEntity resource) {
      return set("resource", nonNull(resource, "resource"));
    }

    /**
     * The action.
     *
     * @param action the value
     * @return this builder
     */
    public Builder action(AuthzenAction action) {
      return set("action", nonNull(action, "action"));
    }

    /**
     * The context.
     *
     * @param context the value
     * @return this builder
     */
    public Builder context(Map<String, ?> context) {
      return set("context", nonNull(context, "context"));
    }

    /**
     * The evaluations. Required.
     *
     * @param evaluations the value
     * @return this builder
     */
    public Builder evaluations(List<EvaluationParams> evaluations) {
      return set("evaluations", listOf(evaluations));
    }

    /**
     * The options.
     *
     * @param options the value
     * @return this builder
     */
    public Builder options(Map<String, ?> options) {
      return set("options", nonNull(options, "options"));
    }

    @Override
    public EvaluationsParams build() {
      require("evaluations", "evaluations");
      return new EvaluationsParams(this);
    }
  }
}
