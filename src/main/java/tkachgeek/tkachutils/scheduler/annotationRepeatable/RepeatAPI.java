package tkachgeek.tkachutils.scheduler.annotationRepeatable;

import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.tkachutils.reflection.ReflectionUtils;
import tkachgeek.tkachutils.scheduler.Tasks;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RepeatAPI {
  static HashMap<JavaPlugin, List<Integer>> tasks = new HashMap<>();
  
  public static void init(JavaPlugin plugin, File file) {
    tasks.put(plugin, new ArrayList<>());
    
    String packageName = plugin.getClass().getPackage().getName();
    
    int i = 0;
    int registered = 0;
    long start = System.currentTimeMillis();
    
    try {
      for (var clazz : ReflectionUtils.getClasses(file, packageName)) {
        i++;
        registered += handle(clazz, plugin);
      }
      
      plugin.getLogger().info("Scanned " + i + " classes, took " + (System.currentTimeMillis() - start) + "ms, registered " + registered + " tasks");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  private static int handle(Class<?> classInfo, JavaPlugin plugin) throws ClassNotFoundException {
    int registered = 0;
    for (Method method : classInfo.getDeclaredMethods()) {
      
      if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0) {
        Repeat annotation = method.getAnnotation(Repeat.class);
        
        if (annotation != null) {
          plugin.getLogger().info("Registered task " + classInfo.getName() + "." + method.getName() + "()");
          registerRepeatable(plugin, method, annotation);
          registered++;
        }
      }
    }
    return registered;
  }
  
  public static void registerRepeatable(JavaPlugin plugin, Method method, Repeat annotation) {
    tasks.computeIfAbsent(plugin, x -> new ArrayList<>());
    
    int id = new RepeatEntry(method, annotation).schedule(plugin);
    tasks.get(plugin).add(id);
  }
  
  public static void unregisterAll(JavaPlugin plugin) {
    if (!tasks.containsKey(plugin)) return;
    
    for (Integer id : tasks.get(plugin)) {
      Tasks.cancelTask(id);
    }
  }
}