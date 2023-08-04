package tkachgeek.tkachutils.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Hologram {
   public static void showTextAsync(String text, int ticksToRemove, Location loc, JavaPlugin plugin) {
      Bukkit.getScheduler().runTask(plugin, () -> spawnHologram(text, ticksToRemove, loc, plugin));
   }

   public static UUID showText(String text, int ticksToRemove, Location loc, JavaPlugin plugin) {
      if (Bukkit.isPrimaryThread()) {
         return spawnHologram(text, ticksToRemove, loc, plugin);
      } else {
         showTextAsync(text, ticksToRemove, loc, plugin);
         plugin.getLogger().warning("Asynchronous entity add!");
         return null;
      }
   }

   private static UUID spawnHologram(String text, int ticksToRemove, Location loc, JavaPlugin plugin) {
      return loc.getWorld().spawn(loc, ArmorStand.class, armorStand -> {
         armorStand.setVisible(false);
         armorStand.setCustomName(text);
         armorStand.setCustomNameVisible(true);
         armorStand.setMarker(true);
         armorStand.setCanMove(false);
         armorStand.addScoreboardTag("hologram");
         armorStand.setCanTick(false);
         if (ticksToRemove > 0)
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, armorStand::remove, ticksToRemove);
      }).getUniqueId();
   }
}