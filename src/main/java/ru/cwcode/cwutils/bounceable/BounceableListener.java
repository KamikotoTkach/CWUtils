package ru.cwcode.cwutils.bounceable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.WeakHashMap;

public class BounceableListener implements Listener {
  private static boolean isRegistered = false;
  private static final Map<Entity, Bounceable<? extends Entity>> bouncedEntities = new WeakHashMap<>();
  
  private BounceableListener() {
  }
  
  @EventHandler
  void onChange(EntityChangeBlockEvent event) {
    Entity entity = event.getEntity();
    if (entity.getType() != EntityType.FALLING_BLOCK) return;
    
    Bounceable<? extends Entity> bounceable = bouncedEntities.remove(entity);
    if (bounceable == null) return;
    
    event.setCancelled(true);
    entity.remove();
    
    bounceable.onLanding(event.getBlock().getLocation());
  }
  
  public static void addBouncedEntity(Entity entity, Bounceable<? extends Entity> bounceable) {
    if (!isRegistered) return;
    bouncedEntities.put(entity, bounceable);
  }
  
  public static void register(JavaPlugin plugin) {
    if (isRegistered) return;
    
    isRegistered = true;
    Bukkit.getPluginManager().registerEvents(new BounceableListener(), plugin);
  }
}
