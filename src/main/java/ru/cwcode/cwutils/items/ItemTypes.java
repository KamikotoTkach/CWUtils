package ru.cwcode.cwutils.items;

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
    return item == null ? ItemType.NONE : getItemType(item.getType());
  }
  
  public static Instruments getToolType(ItemStack item) {
    return item == null ? Instruments.NONE : getToolType(item.getType());
  }
  
  public static ItemType getItemType(Material itemType) {
    return itemType == null ? ItemType.NONE : mappedTypes.getOrDefault(itemType, ItemType.NONE);
  }
  
  public static Instruments getToolType(Material itemType) {
    return itemType == null ? Instruments.NONE : mappedInstruments.getOrDefault(itemType, Instruments.NONE);
  }
  
  public static boolean isSword(ItemStack item) {
    return item != null && Instruments.SWORD.contains(item.getType());
  }
  
  public static boolean isAxe(ItemStack item) {
    return item != null && Instruments.AXE.contains(item.getType());
  }
  
  public static boolean isHoe(ItemStack item) {
    return item != null && Instruments.HOE.contains(item.getType());
  }
  
  public static boolean isShovel(ItemStack item) {
    return item != null && Instruments.SHOVEL.contains(item.getType());
  }
  
  public static boolean isPickaxe(ItemStack item) {
    return item != null && Instruments.PICKAXE.contains(item.getType());
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
    NONE(false),
    SWORD(true),
    HOE(true),
    PICKAXE(true),
    SHOVEL(true),
    AXE(true);
    
    final Set<Material> values = new HashSet<>();
    
    Instruments(boolean shouldTryToFoundItems) {
      if (!shouldTryToFoundItems) return;
      
      Arrays.stream(Material.values()).filter(x -> x.name().endsWith("_" + this.name())).forEach(e -> {
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
    
    ItemType(boolean shouldTryToFoundItems) {
      if (!shouldTryToFoundItems) return;
      
      Arrays.stream(Material.values()).filter(x -> x.name().startsWith(this.name() + "_")).forEach(x -> mappedTypes.put(x, this));
    }
  }
}
