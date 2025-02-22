package ru.cwcode.cwutils.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import ru.cwcode.cwutils.entity.DamageCalculator;

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
  public static int getItemAmount(Player player, ItemStack itemStack) {
    int count = 0;
    for (int i = 0; i < player.getInventory().getSize(); i++) {
      ItemStack stack = player.getInventory().getItem(i);
      if (stack == null)
        continue;
      if (itemStack.isSimilar(stack)) {
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
   * Удаляет нужное количество предметов у игрока, если у него столько есть и возвращает true, иначе false и не удаляет.
   */
  public static boolean removeItems(Player player, Material material, int amount) {
    if (getItemAmount(player, material) >= amount) {
      clearItemsForce(player, material, amount);
      return true;
    }
    return false;
  }
  
  /**
   * Считает количество предметов у игрока
   */
  public static int getItemAmount(Player player, Material material) {
    int count = 0;
    for (int i = 0; i < player.getInventory().getSize(); i++) {
      ItemStack stack = player.getInventory().getItem(i);
      if (stack == null)
        continue;
      if (stack.getType() == material) {
        count += stack.getAmount();
      }
    }
    return count;
  }
  
  /**
   * Очищает определённое количество предметов у игрока, не взирая на наличие
   */
  public static void clearItemsForce(Player player, Material material, int amount) {
    for (int i = 0; i < player.getInventory().getSize(); i++) {
      ItemStack stack = player.getInventory().getItem(i);
      if (stack == null)
        continue;
      if (material == stack.getType()) {
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
      List<Player> list = new ArrayList<>();
      for (Player x : player.getWorld().getNearbyPlayers(player.getLocation(), radius)) {
        if (x != player) {
          list.add(x);
        }
      }
      return (List<T>) list;
    } else {
      return new ArrayList<>(player.getWorld().getNearbyEntitiesByType(type, player.getLocation(), radius));
    }
  }
  
  /**
   * Толкает предмет в сторону игрока
   */
  public static void pushItemToPlayer(Player player, Item item, JavaPlugin plugin) {
    item.setVelocity(new Vector(0, 0.45, 0));
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      item.setVelocity(player.getLocation().add(0, 1.35, 0)
                             .subtract(item.getLocation())
                             .toVector().normalize().multiply(0.65));
    }, 5);
  }
  
  /**
   * @return значение текстуры из профиля игрока
   */
  public static String getTextureValue(PlayerProfile playerProfile) {
    String value = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDVhZGI2ZmZhMmM1YzBlMzUwYzI4NDk5MTM4YTU1NjY0NDFkN2JjNTczZGUxOTg5ZmRlMjEyZmNiMTk2NjgyNiJ9fX0=";
    if (playerProfile != null) {
      for (ProfileProperty profileProperty : playerProfile.getProperties()) {
        if (profileProperty.getName().equals("textures")) {
          value = profileProperty.getValue();
        }
      }
    }
    
    return value;
  }
  
  public static void setMainItemCooldown(Player player, int ticks) {
    player.setCooldown(player.getInventory().getItemInMainHand().getType(), ticks);
  }
  
  public static boolean isMainItemCooldown(Player player) {
    return player.hasCooldown(player.getInventory().getItemInMainHand().getType());
  }
  
  public static boolean isTargeting(Player player, Location target, double expand, double distance) {
    Location eyeLocation = player.getEyeLocation();
    
    BoundingBox boundingBox = new BoundingBox(target.getX(), target.getY(), target.getZ(),
                                              target.getX(), target.getY(), target.getZ());
    boundingBox.expand(expand);
    
    RayTraceResult rayTraceResult = boundingBox.rayTrace(eyeLocation.toVector(), eyeLocation.getDirection(), distance);
    
    return rayTraceResult != null;
  }
  
  /**
   * Calculate amount of EXP needed to level up
   */
  public static int getExpToLevelUp(int level) {
    if (level <= 15) {
      return 2 * level + 7;
    } else if (level <= 30) {
      return 5 * level - 38;
    } else {
      return 9 * level - 158;
    }
  }
  
  /**
   * Calculate total experience up to a level
   */
  public static int getExpAtLevel(int level) {
    if (level <= 16) {
      return (int) (Math.pow(level, 2) + 6 * level);
    } else if (level <= 31) {
      return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360.0);
    } else {
      return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220.0);
    }
  }
  
  /**
   * Calculate player's current EXP amount
   */
  public static int getPlayerExp(Player player) {
    int exp = 0;
    int level = player.getLevel();
    
    exp += getExpAtLevel(level);
    
    exp += Math.round(getExpToLevelUp(level) * player.getExp());
    
    return exp;
  }
  
  /**
   * Give or take EXP
   */
  public static int changePlayerExp(Player player, int exp) {
    int currentExp = getPlayerExp(player);
    
    player.setExp(0);
    player.setLevel(0);
    
    int newExp = currentExp + exp;
    player.giveExp(newExp);
    
    return newExp;
  }
  
  /**
   * Damage player with armor, toughness and resistance effect calculation
   */
  public static void damagePlayer(Player player, double damage) {
    double points = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
    double toughness = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
    PotionEffect effect = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    int resistance = effect == null ? 0 : effect.getAmplifier();
    int epf = DamageCalculator.getEPF(player.getInventory());
    
    player.damage(DamageCalculator.calculateDamageApplied(damage, points, toughness, resistance, epf));
  }
}
