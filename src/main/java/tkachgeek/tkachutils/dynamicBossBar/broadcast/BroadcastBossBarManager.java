package tkachgeek.tkachutils.dynamicBossBar.broadcast;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import tkachgeek.tkachutils.scheduler.Scheduler;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BroadcastBossBarManager {
  ConcurrentLinkedQueue<BroadcastBossBar> bars = new ConcurrentLinkedQueue<>();
  
  JavaPlugin plugin;
  
  public BroadcastBossBarManager(JavaPlugin plugin, boolean async, int delay) {
    this.plugin = plugin;
    
    Scheduler.create()
             .async(async)
             .infinite()
             .perform(this::tick)
             .register(plugin, delay);
  }
  
  public void add(BroadcastBossBar bar) {
    removeBar(bar.getUUID());
    bars.add(bar);
  }
  
  private void tick() {
    if (bars == null || bars.isEmpty()) return;
    
    bars.removeIf(bar -> {
      if (bar.getShouldRemove().get()) {
        bar.hideAll();
        return true;
      } else {
        bar.update();
        return false;
      }
    });
  }
  
  public void removeBar(UUID bar) {
    getBossBarEntries().removeIf(x -> {
      if (x.getUUID().equals(bar)) {
        x.hideAll();
        return true;
      }
      return false;
    });
  }
  
  @NotNull
  public Queue<BroadcastBossBar> getBossBarEntries() {
    return bars;
  }
  
  public void removeBar(BroadcastBossBar bar) {
    getBossBarEntries().removeIf(x -> {
      if (x.getUUID().equals(bar.getUUID())) {
        x.hideAll();
        return true;
      }
      return false;
    });
  }
}
