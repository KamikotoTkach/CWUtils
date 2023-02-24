package tkachgeek.tkachutils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tkachgeek.tkachutils.collections.EnumUtils;

public enum ItemType {
   NONE,
   WOODEN,
   STONE,
   LEATHER,
   CHAINMAIL,
   GOLDEN,
   IRON,
   DIAMOND,
   NETHERITE;

   public static ItemType getItemType(ItemStack item) {
      if (item == null) {
         return NONE;
      }

      String[] itemType = item.getType().name().split("_", 2);
      return EnumUtils.getEnumInstance(values(), itemType[0]).orElse(NONE);
   }

   public static boolean isSword(ItemStack item) {
      if (item == null) {
         return false;
      }

      String[] itemType = item.getType().name().split("_", 2);
      return itemType[itemType.length - 1].equals("SWORD");
   }

   public static boolean isAxe(ItemStack item) {
      if (item == null) {
         return false;
      }

      String[] itemType = item.getType().name().split("_", 2);
      return itemType[itemType.length - 1].equals("AXE");
   }

   public static boolean isTool(ItemStack item) {
      if (item == null) {
         return false;
      }


      String[] itemType = item.getType().name().split("_", 2);
      return itemType[itemType.length - 1].equals("SHOVEL")
            || itemType[itemType.length - 1].equals("PICKAXE")
            || itemType[itemType.length - 1].equals("AXE")
            || itemType[itemType.length - 1].equals("HOE");
   }

   public static boolean isTrident(ItemStack item) {
      if (item == null) {
         return false;
      }

      Material material = item.getType();
      return material.equals(Material.TRIDENT);
   }
}
