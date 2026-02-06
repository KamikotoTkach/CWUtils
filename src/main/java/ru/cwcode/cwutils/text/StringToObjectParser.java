package ru.cwcode.cwutils.text;

import org.bukkit.Location;
import ru.cwcode.cwutils.text.converter.StringLocationConverter;
import ru.cwcode.cwutils.text.converter.StringObjectConverter;
import ru.cwcode.cwutils.text.converter.StringPrimitiveConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class StringToObjectParser {
  private static final Map<Class<?>, StringObjectConverter<?>> converters = new HashMap<>();
  
  static {
    converters.put(Double.class, (StringPrimitiveConverter<Double>) Double::parseDouble);
    converters.put(Long.class, (StringPrimitiveConverter<Long>) Long::parseLong);
    converters.put(Integer.class, (StringPrimitiveConverter<Integer>) Integer::parseInt);
    converters.put(Float.class, (StringPrimitiveConverter<Float>) Float::parseFloat);
    converters.put(Byte.class, (StringPrimitiveConverter<Byte>) Byte::parseByte);
    converters.put(Boolean.class, (StringPrimitiveConverter<Boolean>) Boolean::parseBoolean);
    
    converters.put(double.class, (StringPrimitiveConverter<Double>) Double::parseDouble);
    converters.put(long.class, (StringPrimitiveConverter<Long>) Long::parseLong);
    converters.put(int.class, (StringPrimitiveConverter<Integer>) Integer::parseInt);
    converters.put(float.class, (StringPrimitiveConverter<Float>) Float::parseFloat);
    converters.put(byte.class, (StringPrimitiveConverter<Byte>) Byte::parseByte);
    converters.put(boolean.class, (StringPrimitiveConverter<Boolean>) Boolean::parseBoolean);
    
    converters.put(Location.class, new StringLocationConverter());
    converters.put(String.class, (StringPrimitiveConverter<String>) s -> s);
  }
  
  public static Set<Class<?>> supported() {
    return converters.keySet();
  }
  
  public static <T> Optional<T> tryParse(String input, Class<T> clazz) {
    return Optional.ofNullable(parse(input, clazz));
  }
  
  public static Optional<String> tryToString(Object o) {
    return Optional.ofNullable(toString(o));
  }
  
  public static Optional<String> tryToStringFancy(Object o) {
    return Optional.ofNullable(toStringFancy(o));
  }
  
  public static <T> T parse(String input, Class<T> clazz) {
    var c = converters.get(clazz);
    if (c == null) return null;
    
    @SuppressWarnings("unchecked")
    T result = (T) c.convertFromString(input);
    
    return result;
  }
  
  public static String toString(Object object) {
    @SuppressWarnings("unchecked")
    StringObjectConverter<Object> c = (StringObjectConverter<Object>) converters.get(object.getClass());
    if (c == null) return null;
    
    return c.convertToString(object);
  }
  
  public static String toStringFancy(Object object) {
    @SuppressWarnings("unchecked")
    StringObjectConverter<Object> c = (StringObjectConverter<Object>) converters.get(object.getClass());
    if (c == null) return null;
    
    return c.convertToStringFancy(object);
  }
}
