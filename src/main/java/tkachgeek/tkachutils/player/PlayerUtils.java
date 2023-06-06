package tkachgeek.tkachutils.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerUtils {

   /**
    * Выдаёт предмет игроку. Если нет места - спавнит под ногами.
    */
   public static void safeGive(Player player, ItemStack item) {
      HashMap<Integer, ItemStack> noSpace = player.getInventory().addItem(item);

      for (ItemStack toDrop : noSpace.values()) {
         player.getWorld().dropItem(player.getLocation(), toDrop);
      }
   }

   /**
    * Удаляет нужное количество предметов у игрока, если у него столько есть и возвращает true, иначе false и не удаляет.
    */
   public static boolean removeItems(Player player, ItemStack itemStack, int amount) {
      if (getItemAmount(player, itemStack) >= amount) {
         clearItemsForce(player, itemStack, amount);
         return true;
      }
      return false;
   }

   /**
    * Считает количество предметов у игрока
    */
   public static int getItemAmount(Player p, ItemStack s) {
      int count = 0;
      for (int i = 0; i < p.getInventory().getSize(); i++) {
         ItemStack stack = p.getInventory().getItem(i);
         if (stack == null)
            continue;
         if (s.isSimilar(stack)) {
            count += stack.getAmount();
         }
      }
      return count;
   }

   /**
    * Очищает определённое количество предметов у игрока, не взирая на наличие
    */
   public static void clearItemsForce(Player player, ItemStack itemStack, int amount) {
      for (int i = 0; i < player.getInventory().getSize(); i++) {
         ItemStack stack = player.getInventory().getItem(i);
         if (stack == null)
            continue;
         if (itemStack.isSimilar(stack)) {
            if (stack.getAmount() == 0)
               break;
            if (stack.getAmount() <= amount) {
               amount = amount - stack.getAmount();
               stack.setAmount(-1);
            }
            if (stack.getAmount() > amount) {
               stack.setAmount(stack.getAmount() - amount);
               amount = 0;
            }
         }
      }
   }

   /**
    * Возвращает ближайших энтити, не считая игрока
    */
   public static List<LivingEntity> getNearbyLivingEntities(Player player, double radius) {
      List<LivingEntity> list = new ArrayList<>();

      for (LivingEntity x : player.getLocation().getNearbyLivingEntities(radius)) {
         if (!x.equals(player)) {
            list.add(x);
         }
      }
      return list;
   }

   /**
    * Возвращает ближайшего энтити, не считая игрока
    */
   public static Optional<LivingEntity> getNearbyLivingEntity(Player player, double radius) {
      boolean seen = false;
      LivingEntity best = null;
      Comparator<LivingEntity> comparator = Comparator.comparingDouble(c -> player.getLocation().distance(c.getLocation()));
      for (LivingEntity x : player
            .getLocation()
            .getNearbyLivingEntities(radius)) {
         if (!x.equals(player)) {
            if (!seen || comparator.compare(x, best) < 0) {
               seen = true;
               best = x;
            }
         }
      }
      return seen ? Optional.of(best) : Optional.empty();
   }

   /**
    * Возвращает ближайших энтити, не считая игрока
    */
   public static <T extends Entity> List<T> getNearbyEntities(Class<T> type, Player player, double radius) {
      if (type == Player.class) {
         List<T> list = new ArrayList<>();
         for (T x : player.getWorld().getNearbyEntitiesByType(type, player.getLocation(), radius)) {
            if (x != player) {
               list.add(x);
            }
         }
         return list;
      } else {
         return new ArrayList<>(player.getWorld().getNearbyEntitiesByType(type, player.getLocation(), radius));
      }
   }

   public static void pushItemToPlayer(Player player, Item item, JavaPlugin plugin) {
      item.setVelocity(new Vector(0, 0.45, 0));
      Bukkit.getScheduler().runTaskLater(plugin, () -> {
         item.setVelocity(player.getLocation().add(0, 1.35, 0)
                                .subtract(item.getLocation())
                                .toVector().normalize().multiply(0.65));
      }, 5);
   }
}
