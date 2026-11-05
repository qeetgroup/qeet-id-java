package com.qeetgroup.qeetid.developer;

/**
 * A secret's value, read by name. Its {@code toString} never shows the value.
 *
 * @param name its name
 * @param value the value
 */
public record SecretValue(String name, String value) {

  /** Every component, with secrets shown as {@code [redacted]}. */
  @Override
  public String toString() {
    return "SecretValue[name=" + name + ", value=[redacted]]";
  }
}
