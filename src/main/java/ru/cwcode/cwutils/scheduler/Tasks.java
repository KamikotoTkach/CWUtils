package ru.cwcode.cwutils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;

public class Tasks {
  private static final ConcurrentHashMap<Integer, AbstractScheduler> tasks = new ConcurrentHashMap<>();
  
  public static void put(int id, AbstractScheduler AbstractScheduler) {
    tasks.put(id, AbstractScheduler);
  }
  
  public static void cancelTasks(JavaPlugin plugin) {
    tasks.values()
         .stream()
         .filter(x -> plugin.equals(x.registrant))
         .map(x -> x.taskId)
         .toList()
         .forEach(Tasks::cancelTaskByBukkitId);
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
  
  public static boolean cancelTaskByBukkitId(int taskId) {
    boolean cancelled = false;
    
    for (var entry : tasks.entrySet()) {
      AbstractScheduler abstractScheduler = entry.getValue();
      
      if (abstractScheduler.taskId == taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
        tasks.remove(entry.getKey(), abstractScheduler);
        cancelled = true;
      }
    }
    
    if (!cancelled) Bukkit.getScheduler().cancelTask(taskId);
    
    return cancelled;
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
