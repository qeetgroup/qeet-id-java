package com.qeetgroup.qeetid.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.qeetgroup.qeetid.Params;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * The SDK's one JSON mapper. Unknown fields are ignored (the API may add some), absent and null
 * collections read as empty, nulls are left out of what is sent (except inside {@link Params},
 * where an explicit null clears a field), and {@link Instant}s are RFC 3339 strings.
 */
public final class Json {
  private static final ObjectMapper MAPPER = create();

  private Json() {}

  private static ObjectMapper create() {
    SimpleModule module = new SimpleModule("qeet-id");
    module.addSerializer(Instant.class, new InstantSerializer());
    module.addDeserializer(Instant.class, new InstantDeserializer());
    module.addSerializer(Params.class, new ParamsSerializer());
    JsonSetter.Value asEmpty = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY);
    return JsonMapper.builder()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        .defaultPropertyInclusion(
            JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.ALWAYS))
        .withConfigOverride(List.class, o -> o.setSetterInfo(asEmpty))
        .withConfigOverride(Map.class, o -> o.setSetterInfo(asEmpty))
        .addModule(module)
        .build();
  }

  /** The mapper. */
  public static ObjectMapper mapper() {
    return MAPPER;
  }

  /** The type of {@code raw<parameters...>}. */
  public static JavaType type(Class<?> raw, Class<?>... parameters) {
    return MAPPER.getTypeFactory().constructParametricType(raw, parameters);
  }

  /** The type of {@code raw}. */
  public static JavaType type(Class<?> raw) {
    return MAPPER.constructType(raw);
  }

  /** Serialises {@code value} to JSON bytes. */
  public static byte[] write(Object value) {
    try {
      return MAPPER.writeValueAsBytes(value);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(
          "The value can't be written as JSON: " + e.getMessage(), e);
    }
  }

  /** Reads JSON bytes as {@code type}; throws {@link UncheckedIOException} when they don't fit. */
  @SuppressWarnings("TypeParameterUnusedInFormals") // like Jackson's readValue(JavaType)
  public static <T> T read(byte[] json, JavaType type) {
    try {
      return MAPPER.readValue(json, type);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** Converts a JSON tree to {@code type}. */
  public static <T> T convert(com.fasterxml.jackson.databind.JsonNode node, Class<T> type) {
    try {
      return MAPPER.treeToValue(node, type);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }

  /** Converts a value already read (a map, say) to {@code type}. */
  public static <T> T convert(Object value, Class<T> type) {
    return MAPPER.convertValue(value, type);
  }

  private static final class InstantSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(Instant value, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
      generator.writeString(value.toString());
    }
  }

  private static final class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext context)
        throws IOException {
      if (parser.currentToken() != JsonToken.VALUE_STRING) {
        return (Instant) context.handleUnexpectedToken(Instant.class, parser);
      }
      String text = parser.getText().trim();
      try {
        // ISO_INSTANT accepts an offset as well as Z.
        return Instant.parse(text);
      } catch (DateTimeParseException e) {
        return (Instant)
            context.handleWeirdStringValue(Instant.class, text, "not an RFC 3339 time");
      }
    }
  }

  private static final class ParamsSerializer extends JsonSerializer<Params> {
    @Override
    public void serialize(Params value, JsonGenerator generator, SerializerProvider provider)
        throws IOException {
      generator.writeStartObject();
      for (Map.Entry<String, ?> field : value.toMap().entrySet()) {
        generator.writeFieldName(field.getKey());
        provider.defaultSerializeValue(field.getValue(), generator);
      }
      generator.writeEndObject();
    }
  }
}
