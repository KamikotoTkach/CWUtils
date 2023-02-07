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
    if (before != null) {
      if (mode == ExecutionMode.ASYNC) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> object = before.get());
      } else if (mode == ExecutionMode.SYNC) {
        Bukkit.getScheduler().runTask(plugin, () -> object = before.get());
      } else if (mode == ExecutionMode.INSTANT_SYNC || mode == ExecutionMode.INSTANT_ASYNC) {
        object = before.get();
      }
    }
    
    while (properties.hasNextFrame()) {
      double finalCurrent = properties.nextFrame();
      //properties.debug();
      if (mode == ExecutionMode.ASYNC || mode == ExecutionMode.INSTANT_ASYNC) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> action.accept(finalCurrent, object), properties.frameDelayInTicks());
      } else if (mode == ExecutionMode.SYNC || mode == ExecutionMode.INSTANT_SYNC) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> action.accept(finalCurrent, object), properties.frameDelayInTicks());
      }
    }
    
    if (after != null)
      if (mode == ExecutionMode.ASYNC || mode == ExecutionMode.INSTANT_ASYNC) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> after.accept(object), properties.frameDelayInTicks() + 1);
      } else if (mode == ExecutionMode.SYNC || mode == ExecutionMode.INSTANT_SYNC) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> after.accept(object), properties.frameDelayInTicks() + 1);
      }
  }
  
  public ObjectAnimation<T> after(Consumer<T> after) {
    this.after = after;
    return this;
  }
}
