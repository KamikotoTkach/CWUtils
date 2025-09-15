package ru.cwcode.cwutils.items.similar;

import org.bukkit.inventory.ItemStack;

public interface SimilarMode {
  boolean compare(ItemStack first, ItemStack second);
}
