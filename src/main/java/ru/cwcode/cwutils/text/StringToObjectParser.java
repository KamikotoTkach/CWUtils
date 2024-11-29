package ru.cwcode.cwutils.text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StringToObjectParser {
  static Map<Class<?>, Function<String, Object>> parsers = new HashMap<>();
  
  static {
    registerType(String.class, s -> s);
    
    registerType(int.class, Integer::parseInt);
    registerType(Integer.class, Integer::parseInt);
    
    registerType(long.class, Long::parseLong);
    registerType(Long.class, Long::parseLong);
    
    registerType(float.class, Float::parseFloat);
    registerType(Float.class, Float::parseFloat);
    
    registerType(double.class, Double::parseDouble);
    registerType(Double.class, Double::parseDouble);
    
    registerType(boolean.class, Boolean::parseBoolean);
    registerType(Boolean.class, Boolean::parseBoolean);
  }
  
  public static <T> void registerType(Class<T> clazz, Function<String, T> parser) {
    parsers.put(clazz, (Function<String, Object>) parser);
  }
  
  public static Object parse(String input, Class<?> clazz) {
    Function<String, Object> function = parsers.get(clazz);
    if (function == null) return null;
    
    return function.apply(input);
  }
}
