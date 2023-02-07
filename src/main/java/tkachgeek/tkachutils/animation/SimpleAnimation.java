package tkachgeek.tkachutils.animation;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class SimpleAnimation {
  AnimationProperties properties;
  Consumer<Double> action;
  private Runnable before = null;
  private Runnable after = null;
  private boolean andBack = false;
  
  public SimpleAnimation setProperties(AnimationProperties properties) {
    this.properties = properties;
    return this;
  }
  
  public SimpleAnimation setAction(Consumer<Double> action) {
    this.action = action;
    return this;
  }
  
  public void start(JavaPlugin plugin, ExecutionMode mode) {
    if (before != null) {
      if (mode == ExecutionMode.ASYNC) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> before.run());
      } else if (mode == ExecutionMode.SYNC) {
        Bukkit.getScheduler().runTask(plugin, () -> before.run());
      } else if (mode == ExecutionMode.INSTANT_SYNC || mode == ExecutionMode.INSTANT_ASYNC) {
        before.run();
      }
    }
    
    while (properties.hasNextFrame()) {
      double finalCurrent = properties.nextFrame();
      //properties.debug();
      if (mode == ExecutionMode.ASYNC || mode == ExecutionMode.INSTANT_ASYNC) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> action.accept(finalCurrent), properties.frameDelayInTicks());
        if (andBack) {
          Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> action.accept(finalCurrent), properties.lastFrameDelayInTicks() * 2 - properties.frameDelayInTicks());
        }
      } else if (mode == ExecutionMode.SYNC || mode == ExecutionMode.INSTANT_SYNC) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> action.accept(finalCurrent), properties.frameDelayInTicks());
        if (andBack) {
          Bukkit.getScheduler().runTaskLater(plugin, () -> action.accept(finalCurrent), properties.lastFrameDelayInTicks() * 2 - properties.frameDelayInTicks());
        }
      }
    }
    
    if (after != null)
      if (mode == ExecutionMode.ASYNC || mode == ExecutionMode.INSTANT_ASYNC) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> after.run(), properties.frameDelayInTicks() + 1);
      } else if (mode == ExecutionMode.SYNC || mode == ExecutionMode.INSTANT_SYNC) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> after.run(), properties.frameDelayInTicks() + 1);
      }
    
    if (before != null) before.run();
    
    while (properties.hasNextFrame()) {
      double finalCurrent = properties.nextFrame();
      //properties.debug();
      Bukkit.getScheduler().runTaskLater(plugin, () -> action.accept(finalCurrent), properties.frameDelayInTicks());
      if (andBack) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> action.accept(finalCurrent), properties.lastFrameDelayInTicks() * 2 - properties.frameDelayInTicks());
      }
    }
    
    if (after != null)
      Bukkit.getScheduler().runTaskLater(plugin, () -> after.run(), properties.frameDelayInTicks() + 1 + (andBack ? properties.lastFrameDelayInTicks() : 0));
  }
  
  public SimpleAnimation before(Runnable before) {
    this.before = before;
    return this;
  }
  
  public SimpleAnimation after(Runnable after) {
    this.after = after;
    return this;
  }
  
  public SimpleAnimation andBack() {
    this.andBack = true;
    return this;
  }
}
