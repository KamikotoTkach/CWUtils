package ru.cwcode.cwutils.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class EventUtils {
  public static boolean canInteract(Player player, Location location) {
    return new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), location.getBlock(), BlockFace.SELF, EquipmentSlot.OFF_HAND).callEvent();
  }
  
  public static boolean canPlace(Player player, Location location) {
    return new BlockPlaceEvent(location.getBlock(), location.getBlock().getState(), location.getBlock(), player.getInventory().getItemInMainHand(), player, true, EquipmentSlot.HAND).callEvent();
  }
  
  public static boolean canBreak(Player player, Location location) {
    return new BlockBreakEvent(location.getBlock(), player).callEvent();
  }
  
  public static Optional<Player> getDamager(EntityDamageByEntityEvent event) {
    Player damager;
    if (event.getDamager() instanceof Player) {
      damager = ((Player) event.getDamager());
    } else {
      if (!(event.getDamager() instanceof Projectile)) return Optional.empty();
      if (!(((Projectile) event.getDamager()).getShooter() instanceof Player)) return Optional.empty();
      
      damager = ((Player) event.getDamager());
    }
    
    return Optional.of(damager);
  }
}
