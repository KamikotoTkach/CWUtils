package tkachgeek.tkachutils.scheduler.annotationRepeatable;

import com.google.common.reflect.ClassPath;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RepeatAPI {
  public static void init(JavaPlugin plugin) {
    String packageName = plugin.getClass().getPackage().getName();
    try {
      for (ClassPath.ClassInfo clazz : ClassPath.from(plugin.getClass().getClassLoader()).getAllClasses()) {
        if (clazz.getPackageName().startsWith(packageName)) {
          handle(clazz, plugin);
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static void handle(ClassPath.ClassInfo classInfo, JavaPlugin plugin) throws ClassNotFoundException {
    for (Method method : Class.forName(classInfo.getName()).getDeclaredMethods()) {
      
      if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0) {
        Repeat annotation = method.getAnnotation(Repeat.class);
        
        if (annotation != null) new RepeatEntry(method, annotation).run(plugin);
      }
    }
  }
}
