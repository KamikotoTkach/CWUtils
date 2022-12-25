package tkachgeek.tkachutils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Scheduler<T> {
  private static final HashMap<Integer, Scheduler> tasks = new HashMap<>();
  private static int increment = 0;
  
  private final T anything;
  
  private int taskId = -1;
  private final int id = increment++;

  private boolean asyncTask = false;
  private boolean infinite = false;
  
  private Consumer<T> action = (x) -> {};
  private Consumer<T> lastlyAction = (x) -> {};
  
  private Predicate<T> condition = (x) -> true;
  
  protected Scheduler(T anything) {
    this.anything = anything;
  }
  
  public static <T> Scheduler<T> create(T anything) {
    return new Scheduler<T>(anything);
  }
  
  public static boolean cancelTask(int id) {
    if (tasks.containsKey(id) && (tasks.get(id).taskId != -1)) {
      Bukkit.getScheduler().cancelTask(tasks.get(id).taskId);
      tasks.remove(id);
      return true;
    }
    return false;
  }
  
  public Scheduler<T> perform(Consumer<T> action) {
    this.action = action;
    return this;
  }
  
  public Scheduler<T> until(Predicate<T> condition) {
    this.condition = condition;
    return this;
  }
  
  public Scheduler<T> async() {
    this.asyncTask = true;
    return this;
  }
  
  public Scheduler<T> infinite() {
    this.infinite = true;
    return this;
  }
  
  public Scheduler<T> otherwise(Consumer<T> lastlyAction) {
    this.lastlyAction = lastlyAction;
    return this;
  }
  
  public int register(JavaPlugin plugin, int delay) {
    if (asyncTask) {
      taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::tick, delay, delay).getTaskId();
    } else {
      taskId = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, delay, delay).getTaskId();
    }
    tasks.put(id, this);
    return id;
  }
  
  protected void tick() {
    if (condition.test(anything)) {
      action.accept(anything);
    } else {
      lastlyAction.accept(anything);
      if (!infinite) cancelTask(id);
    }
  }
}
