package tkachgeek.tkachutils.dynamicBossBar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import tkachgeek.tkachutils.scheduler.Scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DynamicBossBarManager {
  HashMap<UUID, Set<BossBarEntry>> bars = new HashMap<>();
  
  JavaPlugin plugin;
  
  public DynamicBossBarManager(JavaPlugin plugin, boolean async, int delay) {
    this.plugin = plugin;
    
    Scheduler.create()
             .async(async)
             .infinite()
             .perform(this::tick)
             .register(plugin, delay);
  }
  
  private void tick() {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      Set<BossBarEntry> bossBarEntries = bars.get(onlinePlayer.getUniqueId());
      if (bossBarEntries == null || bossBarEntries.isEmpty()) continue;
      
      bossBarEntries.removeIf(bar -> {
        if (bar.getShouldRemove().get()) {
          bar.hideAll();
          return true;
        } else {
          bar.update(onlinePlayer);
          return false;
        }
      });
    }
  }
  
  public void send(Player player, BossBarEntry entry) {
    Set<BossBarEntry> bossBarEntries = getBossBarEntries(player);
    
    removeBar(player, entry.getUUID());
    
    bossBarEntries.add(entry);
  }
  
  @NotNull
  public Set<BossBarEntry> getBossBarEntries(Player player) {
    return bars.computeIfAbsent(player.getUniqueId(), uuid -> new HashSet<>());
  }
  
  private void removeBar(Player player, UUID bar) {
    getBossBarEntries(player).removeIf(x -> {
      if (x.getUUID().equals(bar)) {
        x.hideAll();
        return true;
      }
      return false;
    });
  }
}
