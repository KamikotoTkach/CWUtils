package ru.cwcode.cwutils.reflection.injector;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class InjectFields {
  HashMap<Class<?>, Object> fields = new HashMap<>();
  
  public InjectFields(Object... objects) {
    for (Object object : objects) {
      fields.put(object.getClass(), object);
    }
  }
  
  public <T> InjectFields bind(T object, Class<T> as) {
    fields.put(as, object);
    return this;
  }
  
  public @Nullable Object getFieldValue(Class<?> classInfo) {
    return fields.get(classInfo);
  }
}
