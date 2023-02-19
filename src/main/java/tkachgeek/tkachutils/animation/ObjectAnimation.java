package tkachgeek.tkachutils.animation;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectAnimation<T> {
  AnimationProperties properties;
  BiConsumer<Double, T> action;
  Supplier<T> before = null;
  Consumer<T> after = null;
  
  T object;
  
  private ObjectAnimation(Supplier<T> before) {
    this.before = before;
  }
  
  public static <T> ObjectAnimation<T> before(Supplier<T> before) {
    return new ObjectAnimation<>(before);
  }
  
  public ObjectAnimation<T> setProperties(AnimationProperties properties) {
    this.properties = properties;
    return this;
  }
  
  public ObjectAnimation<T> setAction(BiConsumer<Double, T> action) {
    this.action = action;
    return this;
  }
  
  public void start(JavaPlugin plugin, ExecutionMode mode) {
    runBefore(plugin, mode);
    runMain(plugin, mode);
    runAfter(plugin, mode);
  }
  
  private void runAfter(JavaPlugin plugin, ExecutionMode mode) {
    if (after == null) return;
    
    switch (mode) {
      case ASYNC:
      case INSTANT_ASYNC:
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> after.accept(object), properties.frameDelayInTicks() + 1);
        break;
      case SYNC:
      case INSTANT_SYNC:
        Bukkit.getScheduler().runTaskLater(plugin, () -> after.accept(object), properties.frameDelayInTicks() + 1);
        break;
    }
  }
  
  private void runMain(JavaPlugin plugin, ExecutionMode mode) {
    while (properties.hasNextFrame()) {
      double finalCurrent = properties.nextFrame();
      //properties.debug();
      switch (mode) {
        case ASYNC:
        case INSTANT_ASYNC:
          Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> action.accept(finalCurrent, object), properties.frameDelayInTicks());
          break;
        case SYNC:
        case INSTANT_SYNC:
          Bukkit.getScheduler().runTaskLater(plugin, () -> action.accept(finalCurrent, object), properties.frameDelayInTicks());
          break;
      }
    }
  }
  
  private void runBefore(JavaPlugin plugin, ExecutionMode mode) {
    if (before == null) return;
    
    switch (mode) {
      case ASYNC:
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> object = before.get());
        break;
      case SYNC:
        Bukkit.getScheduler().runTask(plugin, () -> object = before.get());
        break;
      case INSTANT_SYNC:
      case INSTANT_ASYNC:
        object = before.get();
        break;
    }
  }
  
  public ObjectAnimation<T> after(Consumer<T> after) {
    this.after = after;
    return this;
  }
}
