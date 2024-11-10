package ru.cwcode.cwutils.items;

import at.syntaxerror.syntaxnbt.NBTCompression;
import at.syntaxerror.syntaxnbt.NBTUtil;
import at.syntaxerror.syntaxnbt.internal.SNBTParser;
import at.syntaxerror.syntaxnbt.tag.TagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    
    byte[] bytes = itemStack.serializeAsBytes();
    ByteArrayInputStream input = new ByteArrayInputStream(bytes);

    try {
      String snbt = NBTUtil.deserialize(input).toString();
      return snbt.substring(5, snbt.length() - 1)
                 .replace(" ", "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Nullable
  public static ItemStack fromSNBT(String snbt) {
    if (snbt == null) return null;
    
    TagCompound parsed = SNBTParser.parse(snbt);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    
    try {
      NBTUtil.serialize(null, parsed, byteArrayOutputStream, NBTCompression.GZIP);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    return ItemStack.deserializeBytes(byteArrayOutputStream.toByteArray());
  }
}
