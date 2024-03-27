package ru.cwcode.cwutils.collections;

import java.lang.reflect.Field;
import java.util.Optional;

public class EnumUtils {
  public static <T extends Enum> Optional<T> getEnumInstance(T[] values, String value) {
    for (T instance : values) {
      if (instance.name().equalsIgnoreCase(value)) {
        return Optional.of(instance);
      }
    }
    return Optional.empty();
  }
  
  public static <E extends Enum> E[] getEnumValues(Class<?> enumClass)
     throws NoSuchFieldException, IllegalAccessException {
    Field f = enumClass.getDeclaredField("$VALUES");
    f.setAccessible(true);
    Object o = f.get(null);
    return (E[]) o;
  }
}
