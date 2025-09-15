package ru.cwcode.cwutils.items.similar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;

public enum SimilarModeImpl implements SimilarMode {
  MATERIAL {
    @Override
    public boolean compare(ItemStack item1, ItemStack item2) {
      if (super.compare(item1, item2)) {
        return item1.getType() == item2.getType();
      }
      
      return false;
    }
  },
  DURABILITY {
    @Override
    public boolean compare(ItemStack item1, ItemStack item2) {
      if (super.compare(item1, item2)) {
        boolean item1Damageable = item1.getItemMeta() instanceof Damageable;
        boolean item2Damageable = item2.getItemMeta() instanceof Damageable;
        if (!item1Damageable && !item2Damageable) return true;
        if (!(item1Damageable && item2Damageable)) return false;
        
        return ((Damageable) item1).getDamage() == ((Damageable) item2).getDamage();
      }
      
      return false;
    }
  },
  DISPLAY_NAME {
    @Override
    public boolean compare(ItemStack item1, ItemStack item2) {
      if (super.compare(item1, item2)) {
        Component item1DisplayName = item1.getItemMeta().displayName();
        Component item2DisplayName = item2.getItemMeta().displayName();
        
        if (item1DisplayName == null && item2DisplayName == null) return true;
        if (item1DisplayName == null || item2DisplayName == null) return false;
        
        String item1DisplayNameText = LegacyComponentSerializer.legacySection().serialize(item1DisplayName);
        String item2DisplayNameText = LegacyComponentSerializer.legacySection().serialize(item2DisplayName);
        
        return item1DisplayNameText.equals(item2DisplayNameText);
      }
      
      return false;
    }
  },
  LORE {
    @Override
    public boolean compare(ItemStack item1, ItemStack item2) {
      if (super.compare(item1, item2)) {
        List<Component> item1Lore = item1.getItemMeta().lore();
        List<Component> item2Lore = item2.getItemMeta().lore();
        
        if (item1Lore == null && item2Lore == null) return true;
        if (item1Lore == null || item2Lore == null) return false;
        
        if (item1Lore.size() != item2Lore.size()) return false;
        if (item1Lore.isEmpty()) return true;
        
        for (int index = 0; index < item1Lore.size(); index++) {
          Component item1LoreLine = item1Lore.get(index);
          Component item2LoreLine = item2Lore.get(index);
          
          if (item1LoreLine == null && item2LoreLine == null) continue;
          if (item1LoreLine == null || item2LoreLine == null) return false;
          
          String item1LoreLineText = LegacyComponentSerializer.legacySection().serialize(item1LoreLine);
          String item2LoreLineText = LegacyComponentSerializer.legacySection().serialize(item2LoreLine);
          
          if (!item1LoreLineText.equals(item2LoreLineText)) return false;
        }
        
        return true;
      }
      
      return false;
    }
  };
  
  public boolean compare(ItemStack item1, ItemStack item2) {
    if (item1 == null) return false;
    if (item2 == null) return false;
    
    return true;
  }
}