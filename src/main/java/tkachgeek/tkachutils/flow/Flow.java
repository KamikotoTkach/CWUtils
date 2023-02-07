package tkachgeek.tkachutils.flow;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Flow<T> {
  List<FlowAction<T>> actions = new ArrayList<>();
  T object;
  private boolean suppressExceptions = true;
  
  public Flow(T object) {
    this.object = object;
  }
  
  public static <T> Flow<T> of(T object) {
    return new Flow<T>(object);
  }
  
  public Flow<T> doNotSuppressExceptions() {
    suppressExceptions = false;
    return this;
  }
  
  public Flow<T> next(Consumer<T> action) {
    this.actions.add(new FlowSimpleAction<>(action));
    return this;
  }
  
  public Flow<T> doUntil(Consumer<T> action, Predicate<T> predicate) {
    this.actions.add(new FlowWhileAction<>(action, predicate));
    return this;
  }
  
  public Flow<T> doIf(Consumer<T> action, Predicate<T> predicate) {
    this.actions.add(new FlowIfAction<>(action, predicate));
    return this;
  }
  
  public Flow<T> sleep(int millis) {
    this.next(x -> {
      try {
        Thread.sleep(millis);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    return this;
  }
  
  public BukkitTask startAsync(JavaPlugin plugin) {
    return Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> startHere(plugin));
  }
  
  public BukkitTask startSync(JavaPlugin plugin) {
    return Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> startHere(plugin));
  }
  
  public BukkitTask startAsync(JavaPlugin plugin, int delay) {
    return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> startHere(plugin), delay);
  }
  
  public BukkitTask startSync(JavaPlugin plugin, int delay) {
    return Bukkit.getScheduler().runTaskLater(plugin, () -> startHere(plugin), delay);
  }
  
  public BukkitTask startAsyncTimer(JavaPlugin plugin, int delay, int period) {
    return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> startHere(plugin), delay, period);
  }
  
  public BukkitTask startSyncTimer(JavaPlugin plugin, int delay, int period) {
    return Bukkit.getScheduler().runTaskTimer(plugin, () -> startHere(plugin), delay, period);
  }
  
  public void startHere(JavaPlugin plugin) {
    try {
      for (var action : actions) {
        action.run(object);
      }
    } catch (Exception ignored) {
      if (!suppressExceptions) ignored.printStackTrace();
    }
  }
}
