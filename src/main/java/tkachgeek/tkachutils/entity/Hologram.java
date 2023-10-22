package tkachgeek.tkachutils.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.tkachutils.messages.Message;

import java.util.concurrent.ExecutionException;

public class Hologram {
   public static ArmorStand showText(Component text, int ticksToRemove, Location loc, JavaPlugin plugin) {
      if (Bukkit.isPrimaryThread()) {
         return spawnHologram(text, ticksToRemove, loc, plugin);
      } else {
         try {
            return Bukkit.getScheduler().callSyncMethod(plugin, () -> Hologram.spawnHologram(text, ticksToRemove, loc, plugin)).get();
         } catch (InterruptedException | ExecutionException exception) {
            plugin.getLogger().warning(exception.getMessage());
            exception.printStackTrace();
            return null;
         }
      }
   }

   public static ArmorStand showText(String text, int ticksToRemove, Location loc, JavaPlugin plugin) {
      return Hologram.showText(Message.from(text), ticksToRemove, loc, plugin);
   }

   private static ArmorStand spawnHologram(Component text, int ticksToRemove, Location loc, JavaPlugin plugin) {
      return loc.getWorld().spawn(loc, ArmorStand.class, armorStand -> {
         armorStand.setVisible(false);
         armorStand.customName(text);
         armorStand.setCustomNameVisible(true);
         armorStand.setMarker(true);
         armorStand.setCanMove(false);
         armorStand.addScoreboardTag("hologram");
         armorStand.setCanTick(false);
         if (ticksToRemove > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, armorStand::remove, ticksToRemove);
         }
      });
   }
}