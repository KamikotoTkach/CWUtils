package tkachgeek.tkachutils.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Supplier;

public class VoidScheduler extends AbstractScheduler {
  JavaPlugin registrant = null;
  
  private volatile Runnable action = () -> {};
  private volatile Runnable lastlyAction = () -> {};
  private volatile Supplier<Boolean> condition = null;
  
  private VoidScheduler() {
  }
  
  public static VoidScheduler create() {
    return new VoidScheduler();
  }
  
  /**
   * Действие
   */
  public VoidScheduler perform(Runnable action) {
    this.action = action;
    return this;
  }
  
  /**
   * Условие, при котором будет совершаться действие
   */
  public VoidScheduler until(Supplier<Boolean> condition) {
    this.condition = condition;
    return this;
  }
  
  /**
   * Совершать действие асинхронно
   */
  public VoidScheduler async() {
    this.asyncTask = true;
    this.blocked = true;
    return this;
  }
  
  public VoidScheduler async(boolean async) {
    this.asyncTask = async;
    this.blocked = async || blocked;
    return this;
  }
  
  /**
   * Не останавливать выполнение, если условие не соблюдено. В таком случае действие не будет выполняться, если условие false
   */
  public VoidScheduler infinite() {
    this.infinite = true;
    return this;
  }
  
  public VoidScheduler infinite(boolean infinite) {
    this.infinite = infinite;
    return this;
  }
  
  /**
   * Действие, выполняющееся вместо основного, когда условие false
   */
  public VoidScheduler otherwise(Runnable lastlyAction) {
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
    
    registrant = plugin;
    
    Tasks.put(id, this);
    
    return id;
  }
  
  private void tick() {
    if (blocked && running) return;
    
    if (condition == null) {
      runWithCancelling(action);
    } else if (condition.get()) {
      run(action);
    } else {
      runWithCancelling(lastlyAction);
    }
  }
  
  private void runWithCancelling(Runnable action) {
    try {
      running = true;
      action.run();
      if (!infinite) Tasks.cancelTask(id);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      running = false;
    }
  }
  
  private void run(Runnable action) {
    try {
      running = true;
      action.run();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      running = false;
    }
  }
}
