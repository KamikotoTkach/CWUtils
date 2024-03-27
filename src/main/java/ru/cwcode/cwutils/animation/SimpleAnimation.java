package ru.cwcode.cwutils.animation;

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
    runBefore(plugin, mode);
    runMain(plugin, mode);
    runAfter(plugin, mode);
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
  
  private void runAfter(JavaPlugin plugin, ExecutionMode mode) {
    if (after == null) return;
    
    switch (mode) {
      case ASYNC:
      case INSTANT_ASYNC:
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> after.run(), properties.frameDelayInTicks() + 1);
        break;
      case SYNC:
      case INSTANT_SYNC:
        Bukkit.getScheduler().runTaskLater(plugin, () -> after.run(), properties.frameDelayInTicks() + 1);
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
          Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> action.accept(finalCurrent), properties.frameDelayInTicks());
          if (andBack) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> action.accept(finalCurrent), properties.lastFrameDelayInTicks() * 2 - properties.frameDelayInTicks());
          }
          break;
        case SYNC:
        case INSTANT_SYNC:
          Bukkit.getScheduler().runTaskLater(plugin, () -> action.accept(finalCurrent), properties.frameDelayInTicks());
          if (andBack) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> action.accept(finalCurrent), properties.lastFrameDelayInTicks() * 2 - properties.frameDelayInTicks());
          }
          break;
      }
    }
  }
  
  private void runBefore(JavaPlugin plugin, ExecutionMode mode) {
    if (before == null) return;
    
    switch (mode) {
      case ASYNC:
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> before.run());
        break;
      case SYNC:
        Bukkit.getScheduler().runTask(plugin, () -> before.run());
        break;
      case INSTANT_SYNC:
      case INSTANT_ASYNC:
        before.run();
        break;
    }
  }
}
