package tkachgeek.tkachutils.items;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;

public enum SimilarMode {
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
   DISPLAYNAME {
      @Override
      public boolean compare(ItemStack item1, ItemStack item2) {
         if (super.compare(item1, item2)) {
            Component item1DisplayName = item1.getItemMeta().displayName();
            Component item2DisplayName = item2.getItemMeta().displayName();

            if (item1DisplayName == null && item2DisplayName == null) return true;
            if (item1DisplayName == null || item2DisplayName == null) return false;

            return item1DisplayName.equals(item2DisplayName);
         }

         return false;
      }
   },
   LORE{
      @Override
      public boolean compare(ItemStack item1, ItemStack item2) {
         if (super.compare(item1, item2)) {
            List<Component> item1Lore = item1.getItemMeta().lore();
            List<Component> item2Lore = item2.getItemMeta().lore();

            if (item1Lore == null && item2Lore == null) return true;
            if (item1Lore == null || item2Lore == null) return false;

            return item1Lore.equals(item2Lore);
         }

         return false;
      }
   };

   public boolean compare(ItemStack item1, ItemStack item2) {
      return item1 != null && item2 != null;
   }
}