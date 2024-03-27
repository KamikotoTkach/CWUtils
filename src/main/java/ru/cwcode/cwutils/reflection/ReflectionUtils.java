package ru.cwcode.cwutils.reflection;

import java.io.File;
import java.lang.reflect.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionUtils {
  
  public static <T> T getNewInstance(Class<T> type, Object... parameters) {
    try {
      Class<?>[] classes = new Class<?>[parameters.length];
      for (int i = 0; i < parameters.length; i++) {
        classes[i] = parameters[i].getClass();
      }
      Constructor<T> constructor = type.getConstructor(classes);
      constructor.setAccessible(true);
      
      return constructor.newInstance(parameters);
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
  
  public static Set<Class<?>> getClasses(File jarFile, String packageName) {
    return getClasses(jarFile, packageName, name -> true);
  }
  
  public static Set<Class<?>> getClasses(File jarFile, String packageName, Predicate<String> filter) {
    Set<Class<?>> classes = new HashSet<>();
    try {
      JarFile file = new JarFile(jarFile);
      for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements(); ) {
        JarEntry jarEntry = entry.nextElement();
        String name = jarEntry.getName().replace("/", ".");
        if (name.startsWith(packageName) && name.endsWith(".class") && filter.test(name))
          classes.add(Class.forName(name.substring(0, name.length() - 6)));
      }
      file.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return classes;
  }
  
  public static boolean tryToInvokeStaticMethod(Class<?> classInfo, String methodName) {
    try {
      Method load;
      load = classInfo.getDeclaredMethod(methodName);
      
      if (!Modifier.isStatic(load.getModifiers()) || load.getParameterCount() != 0) {
        return false;
      }
      
      load.invoke(null);
    } catch (NoSuchMethodException | InvocationTargetException |
             IllegalAccessException e) {
      return false;
    }
    
    return true;
  }
}
