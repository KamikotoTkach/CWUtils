package ru.cwcode.cwutils.items.compare;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.items.ItemStackUtils;
import ru.cwcode.cwutils.items.similar.SimilarMode;

public interface CompareService {
  boolean supports(ItemStack itemStack);
  
  default boolean compare(ItemStack first, ItemStack second, SimilarMode... modes) {
    return ItemStackUtils.isSimilar(first, second, modes);
  }
}
