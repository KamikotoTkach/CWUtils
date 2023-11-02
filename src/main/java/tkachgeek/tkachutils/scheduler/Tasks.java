package tkachgeek.tkachutils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Tasks {
  private static final ConcurrentHashMap<Integer, AbstractScheduler> tasks = new ConcurrentHashMap<>();
  
  public static void put(int id, AbstractScheduler AbstractScheduler) {
    tasks.put(id, AbstractScheduler);
  }
  
  public static void cancelTasks(JavaPlugin plugin) {
    tasks.values()
         .stream()
         .filter(x -> plugin.equals(x.registrant))
         .forEach(x -> cancelTask(x.taskId));
  }
  
  public static boolean cancelTask(int id) {
    AbstractScheduler abstractScheduler = get(id);
    
    if (has(id) && abstractScheduler.taskId != -1) {
      Bukkit.getScheduler().cancelTask(abstractScheduler.taskId);
      remove(id);
      return true;
    }
    return false;
  }
  
  public static AbstractScheduler get(int id) {
    return tasks.get(id);
  }
  
  public static boolean has(int id) {
    return tasks.containsKey(id);
  }
  
  public static void remove(int id) {
    tasks.remove(id);
  }
}
