package tkachgeek.tkachutils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtils {
  
  public static <T> T getNewInstance(Class<T> type, Object... parameters) {
    try {
      Class<?>[] classes = new Class<?>[parameters.length];
      for (int i = 0; i < parameters.length; i++) {
        classes[i] = parameters[i].getClass();
      }
      return type.getConstructor(classes).newInstance(parameters);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static <T> T getFieldValue(Object object, String fieldName, Class<T> fieldType) {
    try {
      Field field = object.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return fieldType.cast(field.get(object));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }
}
