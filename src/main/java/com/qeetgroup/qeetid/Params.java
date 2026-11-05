package com.qeetgroup.qeetid;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import org.jspecify.annotations.Nullable;

/**
 * The parameters of a call: a request body or a list's filters. Each subclass has a builder whose
 * setters name the fields; only the fields you set are sent, and setting one to null sends an
 * explicit null, which clears it on an update.
 *
 * <pre>{@code
 * UserUpdateParams params = UserUpdateParams.builder().displayName("Meera Iyer").phone(null).build();
 * }</pre>
 *
 * <p>Params are immutable and safe to share. {@link #toString()} never shows a password, secret or
 * token.
 */
public abstract class Params {
  private final Map<String, @Nullable Object> fields;
  private final Set<String> sensitive;

  /**
   * Creates the parameters from a builder.
   *
   * @param builder the builder
   */
  protected Params(Builder<?, ?> builder) {
    this.fields = Collections.unmodifiableMap(new LinkedHashMap<>(builder.fields));
    this.sensitive = Set.copyOf(builder.sensitive);
  }

  /**
   * The fields as they are sent, keyed by the API's names, in the order they were set.
   *
   * @return the fields; a null value is sent as an explicit null
   */
  public final Map<String, @Nullable Object> toMap() {
    return fields;
  }

  // Two kinds of parameters with the same fields are still different requests.
  @SuppressWarnings("EqualsGetClass")
  @Override
  public final boolean equals(@Nullable Object other) {
    return other != null
        && other.getClass() == getClass()
        && fields.equals(((Params) other).fields);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(getClass(), fields);
  }

  /** The fields, with passwords, secrets and tokens shown as {@code [redacted]}. */
  @Override
  public final String toString() {
    StringJoiner text = new StringJoiner(", ", getClass().getSimpleName() + "{", "}");
    fields.forEach((name, value) ->
        text.add(name + "=" + (sensitive.contains(name) && value != null ? "[redacted]" : value)));
    return text.toString();
  }

  /**
   * Builds parameters. Not safe for concurrent use; the parameters it builds are.
   *
   * @param <P> the parameters built
   * @param <B> the builder itself
   */
  public abstract static class Builder<P extends Params, B extends Builder<P, B>> {
    private final Map<String, @Nullable Object> fields = new LinkedHashMap<>();
    private final Set<String> sensitive = new LinkedHashSet<>();

    /** Creates an empty builder. */
    protected Builder() {}

    /**
     * Sets a field. Collections and maps are copied.
     *
     * @param name the API's name of the field
     * @param value its value, or null to send an explicit null
     * @return this builder
     */
    protected final B set(String name, @Nullable Object value) {
      fields.put(name, copy(value));
      return self();
    }

    /**
     * Sets a field that must never be printed, such as a password.
     *
     * @param name the API's name of the field
     * @param value its value, or null to send an explicit null
     * @return this builder
     */
    protected final B setSecret(String name, @Nullable Object value) {
      sensitive.add(name);
      return set(name, value);
    }

    /**
     * Fails unless a required field was set to a value.
     *
     * @param name the API's name of the field
     * @param setter the setter that sets it, for the message
     * @throws IllegalStateException when it wasn't
     */
    protected final void require(String name, String setter) {
      if (fields.get(name) == null) {
        throw new IllegalStateException(setter + " is required.");
      }
    }

    /**
     * Builds the parameters.
     *
     * @return the parameters
     * @throws IllegalStateException when a required field is missing
     */
    public abstract P build();

    @SuppressWarnings("unchecked")
    private B self() {
      return (B) this;
    }

    private static @Nullable Object copy(@Nullable Object value) {
      if (value instanceof Collection<?> collection) {
        return Collections.unmodifiableList(new java.util.ArrayList<>(collection));
      }
      if (value instanceof Map<?, ?> map) {
        return Collections.unmodifiableMap(new LinkedHashMap<>(map));
      }
      return value;
    }
  }

  /**
   * Requires a value, with the setter's name in the message.
   *
   * @param value the value
   * @param name the setter's parameter name
   * @param <T> its type
   * @return the value
   */
  protected static <T> T nonNull(@Nullable T value, String name) {
    return Objects.requireNonNull(value, name);
  }

  /**
   * An immutable copy of a list, refusing null items.
   *
   * @param values the list
   * @param <T> the item type
   * @return the copy
   */
  protected static <T> List<T> listOf(Collection<? extends T> values) {
    return List.copyOf(values);
  }
}
