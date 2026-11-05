package com.qeetgroup.qeetid.access;

import com.qeetgroup.qeetid.Params;

/** Paging, and the object type, for the objects the relationship tuples name. */
public final class RelationObjectListParams extends Params {
  private RelationObjectListParams(Builder builder) {
    super(builder);
  }

  /**
   * Starts building {@link RelationObjectListParams}.
   *
   * @return a builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builds {@link RelationObjectListParams}. */
  public static final class Builder extends Params.Builder<RelationObjectListParams, Builder> {
    private Builder() {}

    /**
     * How many items per page. Each list documents its own default and maximum.
     *
     * @param limit the value
     * @return this builder
     */
    public Builder limit(int limit) {
      return set("limit", limit);
    }

    /**
     * Where to start: a page's {@code nextCursor}. Omit for the first page.
     *
     * @param cursor the value
     * @return this builder
     */
    public Builder cursor(String cursor) {
      return set("cursor", nonNull(cursor, "cursor"));
    }

    /**
     * Its type. Required.
     *
     * @param type the value
     * @return this builder
     */
    public Builder type(String type) {
      return set("type", nonNull(type, "type"));
    }

    @Override
    public RelationObjectListParams build() {
      require("type", "type");
      return new RelationObjectListParams(this);
    }
  }
}
