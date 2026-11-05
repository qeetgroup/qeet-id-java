package com.qeetgroup.qeetid.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Makes ES256 keys, JWKS documents and signed tokens for tests, with the JDK alone. */
final class Jwts {
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();

  private Jwts() {}

  record Key(String kid, KeyPair pair) {}

  static Key key(String kid) {
    try {
      KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
      generator.initialize(new ECGenParameterSpec("secp256r1"));
      return new Key(kid, generator.generateKeyPair());
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    }
  }

  static Map<String, Object> jwk(Key key) {
    ECPublicKey pub = (ECPublicKey) key.pair().getPublic();
    Map<String, Object> jwk = new LinkedHashMap<>();
    jwk.put("kty", "EC");
    jwk.put("crv", "P-256");
    jwk.put("kid", key.kid());
    jwk.put("use", "sig");
    jwk.put("alg", "ES256");
    jwk.put("x", B64.encodeToString(fixed(pub.getW().getAffineX())));
    jwk.put("y", B64.encodeToString(fixed(pub.getW().getAffineY())));
    return jwk;
  }

  static String jwks(List<Map<String, Object>> jwks) {
    return json(Map.of("keys", jwks));
  }

  static String token(Key key, Map<String, Object> header, Map<String, Object> claims) {
    Map<String, Object> h = new LinkedHashMap<>();
    h.put("alg", "ES256");
    h.put("typ", "JWT");
    h.put("kid", key.kid());
    h.putAll(header);
    h.values().removeIf(v -> v == null);
    String input =
        B64.encodeToString(json(h).getBytes(StandardCharsets.UTF_8))
            + "."
            + B64.encodeToString(json(claims).getBytes(StandardCharsets.UTF_8));
    try {
      Signature signer = Signature.getInstance("SHA256withECDSAinP1363Format");
      signer.initSign(key.pair().getPrivate());
      signer.update(input.getBytes(StandardCharsets.US_ASCII));
      return input + "." + B64.encodeToString(signer.sign());
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    }
  }

  static String json(Object value) {
    try {
      return MAPPER.writeValueAsString(value);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private static byte[] fixed(BigInteger value) {
    byte[] raw = value.toByteArray();
    if (raw.length == 32) {
      return raw;
    }
    byte[] out = new byte[32];
    if (raw.length > 32) {
      return Arrays.copyOfRange(raw, raw.length - 32, raw.length);
    }
    System.arraycopy(raw, 0, out, 32 - raw.length, raw.length);
    return out;
  }
}
