package tkachgeek.tkachutils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ItemTypes {
  
  final static HashMap<Material, ItemType> mappedTypes = new HashMap<>();
  final static HashMap<Material, Instruments> mappedInstruments = new HashMap<>();
  
  public static ItemType getItemType(ItemStack item) {
    return item == null ? ItemType.NONE : mappedTypes.getOrDefault(item.getType(), ItemType.NONE);
  }
  
  public static boolean isSword(ItemStack item) {
    return item != null && Instruments.SWORDS.contains(item.getType());
  }
  
  public static boolean isAxe(ItemStack item) {
    return item != null && Instruments.AXES.contains(item.getType());
  }
  
  public static boolean isHoe(ItemStack item) {
    return item != null && Instruments.HOES.contains(item.getType());
  }
  
  public static boolean isShovel(ItemStack item) {
    return item != null && Instruments.SHOVELS.contains(item.getType());
  }
  
  public static boolean isPickaxe(ItemStack item) {
    return item != null && Instruments.PICKAXES.contains(item.getType());
  }
  
  public static boolean isTool(ItemStack item) {
    return item != null && mappedInstruments.containsKey(item.getType());
  }
  
  public static boolean isTrident(ItemStack item) {
    return item != null && item.getType() == Material.TRIDENT;
  }
  
  public static boolean isUsable(ItemStack item) {
    return item != null && isUsable(item.getType());
  }
  
  public static boolean isUsable(Material material) {
    switch (material) {
      case POTION:
      case LINGERING_POTION:
      case SPLASH_POTION:
      case WRITABLE_BOOK:
      case WRITTEN_BOOK:
      case ENDER_EYE:
      case ENDER_PEARL:
      case BOW:
      case CROSSBOW:
        return true;
      default:
        return material.isEdible();
    }
  }
  
  public enum Instruments {
    SWORDS,
    HOES,
    PICKAXES,
    SHOVELS,
    AXES;
    final Set<Material> values = new HashSet<>();
    
    Instruments() {
      Arrays.stream(Material.values()).filter(x -> x.name().endsWith("_" + name())).forEach(e -> {
        values.add(e);
        mappedInstruments.put(e, this);
      });
    }
    
    public boolean contains(Material type) {
      return values.contains(type);
    }
  }
  
  public enum ItemType {
    NONE(false),
    WOODEN(true),
    STONE(true),
    LEATHER(true),
    CHAINMAIL(true),
    GOLDEN(true),
    IRON(true),
    DIAMOND(true),
    NETHERITE(true);
    final HashMap<Material, ItemType> matchingMaterials = new HashMap<>();
    
    ItemType(boolean shouldTryToFoundItems) {
      if (shouldTryToFoundItems) {
        Arrays.stream(Material.values()).filter(x -> x.name().startsWith(name() + "_")).forEach(x -> matchingMaterials.put(x, this));
      }
    }
  }
}
