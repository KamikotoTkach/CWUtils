package tkachgeek.tkachutils.reflection;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ClassScanner {
  File jar;
  List<ClassApplier> classAppliers = new ArrayList<>();
  List<MethodApplier> methodAppliers = new ArrayList<>();
  List<FieldApplier> fieldAppliers = new ArrayList<>();
  
  Predicate<String> filter = filter -> true;
  public ClassScanner(File jar) {
    this.jar = jar;
  }
  
  public ClassScanner apply(ClassApplier applier) {
    classAppliers.add(applier);
    return this;
  }
  
  public ClassScanner apply(MethodApplier applier) {
    methodAppliers.add(applier);
    return this;
  }
  
  public ClassScanner apply(FieldApplier applier) {
    fieldAppliers.add(applier);
    return this;
  }
  
  public ClassScanner classFilter(Predicate<String> filter) {
    this.filter = filter;
    return this;
  }
  
  public void scan(JavaPlugin plugin) {
    if (classAppliers.isEmpty() && methodAppliers.isEmpty() && fieldAppliers.isEmpty()) return;
    
    String packageName = plugin.getClass().getPackage().getName();
    
    int i = 0;
    long start = System.currentTimeMillis();
    
    try {
      for (var clazz : ReflectionUtils.getClasses(jar, packageName, filter)) {
        i++;
        handle(clazz);
      }
      
      plugin.getLogger().info("Scanned " + i + " classes, took " + (System.currentTimeMillis() - start) + "ms");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    
    classAppliers.clear();
    methodAppliers.clear();
    fieldAppliers.clear();
  }
  
  private void handle(Class<?> classInfo) throws ClassNotFoundException {
    for (ClassApplier classApplier : classAppliers) {
      if (classApplier.predicate.test(classInfo)) {
        classApplier.consumer.accept(classInfo);
      }
    }
    
    for (Method method : classInfo.getDeclaredMethods()) {
      for (MethodApplier x : methodAppliers) {
        if (x.predicate.test(method)) {
          x.consumer.accept(method);
        }
      }
    }
    
    for (Field field : classInfo.getDeclaredFields()) {
      for (FieldApplier x : fieldAppliers) {
        if (x.predicate.test(field)) {
          x.consumer.accept(field);
        }
      }
    }
  }
  
  public static class ClassApplier {
    private final Predicate<Class<?>> predicate;
    private final Consumer<Class<?>> consumer;
    
    public ClassApplier(Predicate<Class<?>> predicate, Consumer<Class<?>> consumer) {
      this.predicate = predicate;
      this.consumer = consumer;
    }
  }
  
  public static class MethodApplier {
    private final Predicate<Method> predicate;
    private final Consumer<Method> consumer;
    
    public MethodApplier(Predicate<Method> predicate, Consumer<Method> consumer) {
      this.predicate = predicate;
      this.consumer = consumer;
    }
  }
  
  private static class FieldApplier {
    private final Predicate<Field> predicate;
    private final Consumer<Field> consumer;
    
    public FieldApplier(Predicate<Field> predicate, Consumer<Field> consumer) {
      this.predicate = predicate;
      this.consumer = consumer;
    }
  }
}
