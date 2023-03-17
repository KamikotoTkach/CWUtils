package tkachgeek.tkachutils.scheduler.annotationRepeatable;

import com.google.common.reflect.ClassPath;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class RepeatAPI {
  public static void init(JavaPlugin plugin) {
    String packageName = plugin.getClass().getPackage().getName();
  
    int i = 0;
    int registered = 0;
    long start = System.currentTimeMillis();
  
    try {
      for (ClassPath.ClassInfo clazz : ClassPath.from(plugin.getClass().getClassLoader()).getTopLevelClassesRecursive(packageName)) {
        i++;
        registered += handle(clazz, plugin);
      }
    
      plugin.getLogger().info("Scanned " + i + " classes, took " + (System.currentTimeMillis() - start) + "ms, registered " + registered + " tasks");
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  private static int handle(ClassPath.ClassInfo classInfo, JavaPlugin plugin) throws ClassNotFoundException {
    int registered = 0;
    for (Method method : classInfo.load().getDeclaredMethods()) {
      
      if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0) {
        Repeat annotation = method.getAnnotation(Repeat.class);
        
        if (annotation != null) {
          plugin.getLogger().info("Registered task " + classInfo.getName() + "/" + method);
          new RepeatEntry(method, annotation).run(plugin);
          registered++;
        }
      }
    }
    return registered;
  }
}