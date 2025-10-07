package ru.cwcode.cwutils.items.compare;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import ru.cwcode.cwutils.items.similar.SimilarMode;

public class PotionCompareService implements CompareService {
  @Override
  public boolean supports(ItemStack itemStack) {
    if (itemStack == null) return false;
    
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta == null) return false;
    
    return itemMeta instanceof PotionMeta;
  }
  
  @Override
  public boolean compare(ItemStack first, ItemStack second, SimilarMode... modes) {
    if (first == null || second == null) return false;
    
    ItemMeta firstMeta = first.getItemMeta();
    ItemMeta secondMeta = second.getItemMeta();
    
    if (!(firstMeta instanceof PotionMeta firstPotionMeta)) return false;
    if (!(secondMeta instanceof PotionMeta secondPotionMeta)) return false;
    
    if (!firstPotionMeta.getBasePotionData().equals(secondPotionMeta.getBasePotionData())) return false;
    if (!firstPotionMeta.getCustomEffects().equals(secondPotionMeta.getCustomEffects())) return false;
    
    return CompareService.super.compare(first, second, modes);
  }
}
