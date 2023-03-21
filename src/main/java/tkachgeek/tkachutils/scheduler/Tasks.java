package tkachgeek.tkachutils.scheduler;

import org.bukkit.Bukkit;

import java.util.concurrent.ConcurrentHashMap;

public class Tasks {
  private static final ConcurrentHashMap<Integer, Scheduler> tasks = new ConcurrentHashMap<>();
  
  public static Scheduler get(int id) {
    return tasks.get(id);
  }
  
  public static void put(int id, Scheduler scheduler) {
    tasks.put(id, scheduler);
  }
  
  public static boolean has(int id) {
    return tasks.containsKey(id);
  }
  
  public static void remove(int id) {
    tasks.remove(id);
  }
  
  public static boolean cancelTask(int id) {
    Scheduler scheduler = get(id);
    if (has(id) && scheduler.taskId != -1) {
      Bukkit.getScheduler().cancelTask(scheduler.taskId);
      remove(id);
      return true;
    }
    return false;
  }
}
