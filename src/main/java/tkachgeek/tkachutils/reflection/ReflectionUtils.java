package tkachgeek.tkachutils.reflection;

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
}
