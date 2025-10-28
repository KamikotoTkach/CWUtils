package ru.cwcode.cwutils.system;

import java.util.Optional;
import java.util.function.Function;

public class EnvUtils {
  public static Optional<Integer> getInt(String key) {
    return getEnv(key, Integer::parseInt);
  }
  
  public static Optional<Long> getLong(String key) {
    return getEnv(key, Long::parseLong);
  }
  
  public static Optional<Double> getDouble(String key) {
    return getEnv(key, Double::parseDouble);
  }
  
  public static Optional<String> getString(String key) {
    return getEnv(key, Function.identity());
  }
  
  public static <E extends Enum<E>> Optional<E> getEnum(String key, Class<E> type) {
    return getEnv(key, val -> Enum.valueOf(type, val));
  }
  
  public static <T> Optional<T> getEnv(String key, Function<String, T> converter) {
    String value = System.getenv(key);
    if (value == null) return Optional.empty();
    try {
      return Optional.ofNullable(converter.apply(value));
    } catch (Exception e) {
      return Optional.empty();
    }
  }
}
