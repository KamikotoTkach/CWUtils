package ru.cwcode.cwutils.items.compare;

import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.items.similar.SimilarMode;

public class ItemCompareService implements CompareService {
  @Override
  public boolean supports(ItemStack itemStack) {
    return itemStack != null;
  }
  
  @Override
  public boolean compare(ItemStack first, ItemStack second, SimilarMode... modes) {
    if (first == null) return false;
    if (second == null) return false;
    
    return CompareService.super.compare(first, second);
  }
}
