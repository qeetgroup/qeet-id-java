package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;
import java.util.Map;

/**
 * An OpenID AuthZEN evaluation request. With {@code resource.type} {@code "permission"}, {@code
 * action.name} is a permission key; otherwise the resource is {@code type:id} and {@code
 * action.name} a relation.
 */
public final class EvaluationParams extends Params {
  private EvaluationParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link EvaluationParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link EvaluationParams}. */
  public static final class Builder extends Params.Builder<EvaluationParams, Builder> {
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

    @Override
    public EvaluationParams build() {
      return new EvaluationParams(this);
    }
  }
}
