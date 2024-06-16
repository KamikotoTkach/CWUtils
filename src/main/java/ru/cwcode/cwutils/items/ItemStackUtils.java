package ru.cwcode.cwutils.items;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.io.SNBTUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.EnumSet;

public class ItemStackUtils {
  @SuppressWarnings("deprecation")
  @Nullable
  public static Material fromLegacyID(int ID, byte Data) {
    for (Material i : EnumSet.allOf(Material.class))
      if (i.getId() == ID) return Bukkit.getUnsafe().fromLegacy(new MaterialData(i, Data));
    return null;
  }
  
  public static ItemStack[] cloneItemStacks(ItemStack[] itemStacks) {
    ItemStack[] items = new ItemStack[itemStacks.length];
    for (int index = 0; index < itemStacks.length; index++) {
      ItemStack item = itemStacks[index];
      if (item != null) {
        item = item.clone();
      }
      
      items[index] = item;
    }
    
    return items;
  }
  
  public static boolean isSimilar(@Nullable ItemStack item1, @Nullable ItemStack item2, @NotNull SimilarMode... modes) {
    for (SimilarMode mode : modes) {
      if (!mode.compare(item1, item2)) return false;
    }
    
    return true;
  }
  
  @Nullable
  public static String toSNBT(ItemStack itemStack) {
    if (itemStack == null) return null;
    
    try {
      return SNBTUtil.toSNBT(new NBTDeserializer().fromBytes(itemStack.serializeAsBytes()).getTag());
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  @Nullable
  public static ItemStack fromSNBT(String snbt) {
    if (snbt == null) return null;
    
    try {
      return ItemStack.deserializeBytes(new NBTSerializer().toBytes(new NamedTag("", SNBTUtil.fromSNBT(snbt))));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
