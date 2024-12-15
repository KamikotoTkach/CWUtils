package ru.cwcode.cwutils.dynamicBossBar.personal.v2;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.cwutils.event.EventHandler;
import ru.cwcode.cwutils.scheduler.Scheduler;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PersonalBossBarManager {
  ConcurrentHashMap<UUID, PersonalBossBar> bars = new ConcurrentHashMap<>();
  
  JavaPlugin plugin;
  
  public PersonalBossBarManager(JavaPlugin plugin, boolean async, int delay) {
    this.plugin = plugin;
    
    Scheduler.create()
             .async(async)
             .infinite()
             .perform(this::tick)
             .register(plugin, delay);
    
    new EventHandler<>(PlayerQuitEvent.class, event -> onQuit(event.getPlayer()), plugin);
  }
  
  public void add(PersonalBossBar personalBossBar) {
    bars.put(personalBossBar.uuid, personalBossBar);
  }
  
  public void remove(PersonalBossBar personalBossBar) {
    bars.remove(personalBossBar.uuid);
    personalBossBar.delete();
  }
  
  protected void onQuit(Player player) {
    bars.values().forEach(personalBossBar -> personalBossBar.remove(player));
  }
  
  protected void tick() {
    Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
    
    bars.entrySet().removeIf(entry -> entry.getValue().shouldRemove());
    
    for (PersonalBossBar personalBossBar : bars.values()) {
      for (Player player : onlinePlayers) {
        personalBossBar.update(player);
      }
    }
  }
}
