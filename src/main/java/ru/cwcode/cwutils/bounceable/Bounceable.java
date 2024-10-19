package ru.cwcode.cwutils.bounceable;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public interface Bounceable<T extends Entity, EVENT extends EntityEvent> {
  
  void onExecute(EVENT event);
  
  default double getVelocityMultiplier() {
    return 1.0;
  }
  
  Class<T> getEntityClass();
  
  default T spawnEntity(Location location, Player player, ItemStack itemStack) {
    return location.getWorld().spawn(location, this.getEntityClass());
  }
  
  default void bounce(Player player, ItemStack itemStack) {
    Location location = player.getEyeLocation();
    Vector velocity = location.getDirection().normalize().multiply(this.getVelocityMultiplier());
    
    T entity = this.spawnEntity(location, player, itemStack);
    entity.setVelocity(velocity);
    
    BounceableListener.addBouncedEntity(entity, this);
    
    itemStack.subtract();
  }
}
