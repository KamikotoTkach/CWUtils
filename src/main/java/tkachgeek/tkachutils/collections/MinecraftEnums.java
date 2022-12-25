package tkachgeek.tkachutils.collections;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

public class MinecraftEnums {
  public static boolean isPotionEffect(String name) {
    return PotionEffectType.getByName(name) != null;
  }
  
  public static boolean isEnchantment(String name) {
    return Enchantment.getByKey(NamespacedKey.minecraft(name)) != null;
  }
  
  public static boolean isMaterial(String name) {
    return Material.getMaterial(name) != null;
  }
  
  public static PotionEffectType getRandomPotionEffect() {
    return CollectionUtils.getRandomArrayEntry(PotionEffectType.values());
  }
  
  public static Material getRandomMaterial() {
    return CollectionUtils.getRandomArrayEntry(Material.values());
  }
  
  public static Enchantment getRandomEnchantment() {
    return CollectionUtils.getRandomArrayEntry(Enchantment.values());
  }
}
