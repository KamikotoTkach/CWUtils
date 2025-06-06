package ru.cwcode.cwutils.items;

import com.saicone.rtag.item.ItemTagStream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

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
  
  public static void editMeta(ItemStack itemStack, Consumer<ItemMeta> editor) {
    if (itemStack == null) return;
    
    ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) return;
    
    itemStack.editMeta(editor);
  }
  
  public static ItemStack removeItalicFont(ItemStack itemStack) {
    if (itemStack == null) return null;
    if (itemStack.lore() == null && !itemStack.getItemMeta().hasDisplayName()) return itemStack;
    
    if (itemStack.getItemMeta().hasDisplayName()) {
      editMeta(itemStack, meta -> {
        meta.displayName(meta.displayName().decoration(TextDecoration.ITALIC, false));
      });
    }
    
    if (itemStack.lore() != null) {
      List<Component> lore = itemStack.lore().stream()
                                      .map(x -> x.decoration(TextDecoration.ITALIC, false))
                                      .toList();
      itemStack.lore(lore);
    }
    
    return itemStack;
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
    
    return ItemTagStream.INSTANCE.toString(itemStack);
  }
  
  @Nullable
  public static ItemStack fromSNBT(String snbt) {
    if (snbt == null) return null;
    
    return ItemTagStream.INSTANCE.fromString(snbt);
  }
  
  public static Component getDisplayNameWithoutBrackets(ItemStack item) {
    if (item == null) return Component.text("null");
    
    Component component = item.displayName();
    if (component instanceof TranslatableComponent tc && tc.args().size() == 1) {
      return tc.args().get(0).style(component.style());
    }
    
    return component;
  }
}
