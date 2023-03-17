package tkachgeek.tkachutils.scheduler;

import java.util.concurrent.ConcurrentHashMap;

public class Tasks {
  private static final ConcurrentHashMap<Integer, Scheduler> tasks = new ConcurrentHashMap<>();
  
  public synchronized static Scheduler get(int id) {
    return tasks.get(id);
  }
  
  public static synchronized void put(int id, Scheduler scheduler) {
    tasks.put(id, scheduler);
  }
  
  public static synchronized boolean has(int id) {
    return tasks.containsKey(id);
  }
  
  public static synchronized void remove(int id) {
    tasks.remove(id);
  }
}
