package tkachgeek.tkachutils.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Hologram {
  
  public static UUID showText(String text, int ticksToRemove, Location loc, JavaPlugin plugin) {
    return loc.getWorld().spawn(loc, ArmorStand.class, armorStand -> {
      armorStand.setVisible(false);
      armorStand.setCustomName(text);
      armorStand.setCustomNameVisible(true);
      armorStand.setMarker(true);
      armorStand.setCanMove(false);
      armorStand.addScoreboardTag("hologram");
      armorStand.setCanTick(false);
      if (ticksToRemove > 0) Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, armorStand::remove, ticksToRemove);
    }).getUniqueId();
  }
}