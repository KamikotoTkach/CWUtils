package ru.cwcode.cwutils.entity;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DamageCalculator {
  public static double calculateDamageApplied(double damage, double armorPoints, double toughness, int resistance, int epf) {
    double withArmorAndToughness = damage * (1 - Math.min(20, Math.max(armorPoints / 5, armorPoints - damage / (2 + toughness / 4))) / 25);
    double withResistance = withArmorAndToughness * (1 - (resistance * 0.2));
    double withEnchants = withResistance * (1 - (Math.min(20.0, epf) / 25));
    return withEnchants;
  }
  
  public static int getEPF(PlayerInventory inv) {
    ItemStack helm = inv.getHelmet();
    ItemStack chest = inv.getChestplate();
    ItemStack legs = inv.getLeggings();
    ItemStack boot = inv.getBoots();
    
    return (helm != null ? helm.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) : 0) +
           (chest != null ? chest.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) : 0) +
           (legs != null ? legs.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) : 0) +
           (boot != null ? boot.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) : 0);
  }
}
