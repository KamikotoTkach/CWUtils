package ru.cwcode.cwutils.reflection.injector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Injector {
  public static void inject(Class<?> classInfo, InjectFields toInject) {
    for (Field field : classInfo.getDeclaredFields()) {
      if (!Modifier.isStatic(field.getModifiers()) || field.getAnnotation(Inject.class) == null) continue;
      
      Object fieldValue = toInject.getFieldValue(field.getType());
      
      if (fieldValue != null) {
        try {
          field.setAccessible(true);
          field.set(null, fieldValue);
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
