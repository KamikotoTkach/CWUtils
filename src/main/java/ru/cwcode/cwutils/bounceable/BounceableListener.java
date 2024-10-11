package ru.cwcode.cwutils.bounceable;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.WeakHashMap;

public class BounceableListener implements Listener {
  private static boolean isRegistered = false;
  private static final Map<Entity, Bounceable<? extends Entity, ? extends EntityEvent>> bouncedEntities = new WeakHashMap<>();
  private static JavaPlugin plugin;
  
  
  private BounceableListener() {
  }
  
  @EventHandler
  void onChange(EntityChangeBlockEvent event) {
    Entity entity = event.getEntity();
    if (entity.getType() != EntityType.FALLING_BLOCK) return;
    
    Bounceable bounceable = bouncedEntities.remove(entity);
    if (bounceable == null) return;
    
    bounceable.onExecute(event);
  }
  
  @EventHandler
  void onEntityRemove(EntityRemoveFromWorldEvent event) {
    Entity entity = event.getEntity();
    if (entity.getType() != EntityType.FALLING_BLOCK) return;
    
    Bounceable bounceable = bouncedEntities.remove(entity);
    if (bounceable == null) return;
    
    Location location = event.getEntity().getLocation().toCenterLocation();
    
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      location.getNearbyEntitiesByType(Item.class, 1).forEach(Item::remove);
    }, 1L);
  }
  
  @EventHandler
  void onExplode(EntityExplodeEvent event) {
    Entity entity = event.getEntity();
    if (entity.getType() != EntityType.PRIMED_TNT) return;
    
    Bounceable bounceable = bouncedEntities.remove(entity);
    if (bounceable == null) return;
    
    bounceable.onExecute(event);
  }
  
  public static void addBouncedEntity(Entity entity, Bounceable<? extends Entity, ? extends EntityEvent> bounceable) {
    if (!isRegistered) return;
    bouncedEntities.put(entity, bounceable);
  }
  
  public static void register(JavaPlugin plugin) {
    if (isRegistered) return;
    
    isRegistered = true;
    BounceableListener.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(new BounceableListener(), plugin);
  }
}
