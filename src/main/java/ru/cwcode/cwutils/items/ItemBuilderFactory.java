package ru.cwcode.cwutils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.server.PaperServerUtils;

public class ItemBuilderFactory {
  public static ItemBuilder of(Material material) {
    if (PaperServerUtils.isVersionBefore1_16_5()) {
      return new ItemBuilderLegacy(material);
    }
    return new ItemBuilder(material);
  }
  
  public static ItemBuilder of(ItemStack itemStack) {
    if (PaperServerUtils.isVersionBefore1_16_5()) {
      return new ItemBuilderLegacy(itemStack);
    }
    return new ItemBuilder(itemStack);
  }
}
