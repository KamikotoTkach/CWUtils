package tkachgeek.tkachutils.scheduler.annotationRepeatable;

import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.tkachutils.scheduler.Scheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RepeatEntry {
  Method method;
  long delay;
  boolean async;
  
  public RepeatEntry(Method method, Repeat annotation) {
    this.method = method;
    this.delay = annotation.delay();
    this.async = annotation.async();
  }
  
  public int schedule(JavaPlugin plugin) {
    Scheduler<Method> scheduler = Scheduler.create(method).infinite();
    if (async) scheduler.async();
    
    scheduler.perform(x -> {
      try {
        x.setAccessible(true);
        x.invoke(null);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    });
    
    return scheduler.register(plugin, (int) delay);
  }
}
