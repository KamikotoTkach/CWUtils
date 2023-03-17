package tkachgeek.tkachutils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Scheduler<T> {
  private static int increment = 0;
  
  private final T anything;
  private final int id = increment++;
  private volatile int taskId = -1;
  private volatile boolean asyncTask = false;
  private volatile boolean infinite = false;
  
  private volatile Consumer<T> action = (x) -> {};
  private volatile Consumer<T> lastlyAction = (x) -> {};
  
  private volatile Predicate<T> condition = null;
  
  protected Scheduler(T anything) {
    this.anything = anything;
  }
  
  public static <T> Scheduler<T> create(T anything) {
    return new Scheduler<T>(anything);
  }
  
  /**
   * Отменяет такс
   */
  public static boolean cancelTask(int id) {
    if (Tasks.has(id) && (Tasks.get(id).taskId != -1)) {
      Bukkit.getScheduler().cancelTask(Tasks.get(id).taskId);
      Tasks.remove(id);
      return true;
    }
    return false;
  }
  
  /**
   * Действие
   */
  public Scheduler<T> perform(Consumer<T> action) {
    this.action = action;
    return this;
  }
  
  /**
   * Условие, при котором будет совершаться действие
   */
  public Scheduler<T> until(Predicate<T> condition) {
    this.condition = condition;
    return this;
  }
  
  /**
   * Совершать действие асинхронно
   */
  public Scheduler<T> async() {
    this.asyncTask = true;
    return this;
  }
  
  /**
   * Не останавливать выполнение, если условие не соблюдено. В таком случае действие не будет выполняться, если условие false
   */
  public Scheduler<T> infinite() {
    this.infinite = true;
    return this;
  }
  
  /**
   * Действие, выполняющееся вместо основного, когда условие false
   */
  public Scheduler<T> otherwise(Consumer<T> lastlyAction) {
    this.lastlyAction = lastlyAction;
    return this;
  }
  
  /**
   * Регистрирует и запускает планировщик
   */
  public int register(JavaPlugin plugin, long delay) {
    if (asyncTask) {
      taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::tick, delay, delay).getTaskId();
    } else {
      taskId = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, delay, delay).getTaskId();
    }
    Tasks.put(id, this);
    return id;
  }
  
  protected void tick() {
    if (condition == null) {
      action.accept(anything);
      if (!infinite) cancelTask(id);
      return;
    }
    
    if (condition.test(anything)) {
      action.accept(anything);
    } else {
      lastlyAction.accept(anything);
      if (!infinite) cancelTask(id);
    }
  }
}
