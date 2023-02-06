package tkachgeek.tkachutils.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tkachgeek.tkachutils.messages.Message;
import tkachgeek.tkachutils.server.ServerUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemFactory {

   protected ItemStack item;
   protected ItemMeta meta;

   public ItemFactory(Material material) {
      item = new ItemStack(material);
      meta = item.getItemMeta();
   }

   public ItemFactory(ItemStack item) {
      this.item = item;
      meta = item.getItemMeta();
   }

   static public ItemFactory of(Material material) {
      return new ItemFactory(material);
   }

   static public ItemFactory of(ItemStack item) {
      return new ItemFactory(item);
   }

   public ItemFactory amount(int amount) {
      item.setAmount(amount);
      return this;
   }

   public boolean hasName() {
      return this.meta.hasDisplayName();
   }

   public Component name() {
      if (!this.hasName()) {
         return Component.translatable(this.item.getType().getTranslationKey())
                         .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
      }

      if (ServerUtils.isVersionBefore1_16_5()) {
         return Message.getInstance(meta.getDisplayName()).get();
      } else {
         return meta.displayName();
      }
   }

   public ItemFactory name(Component name) {
      if (ServerUtils.isVersionBefore1_16_5()) {
         meta.setDisplayName(Message.getInstance(name).toString());
      } else {
         meta.displayName(name);
      }
      return this;
   }

   public boolean hasDescription() {
      return this.meta.hasLore();
   }

   public List<Component> description() {
      if (!this.hasDescription()) {
         return new ArrayList<>();
      }

      if (ServerUtils.isVersionBefore1_16_5()) {
         List<Component> description = new ArrayList<>();
         for (String line : meta.getLore()) {
            description.add(Message.getInstance(line).get());
         }
         return description;
      } else {
         return meta.lore();
      }
   }

   public ItemFactory description(Component... description) {
      return this.description(Arrays.asList(description));
   }

   public ItemFactory description(List<Component> description) {
      if (ServerUtils.isVersionBefore1_16_5()) {
         List<String> old_lore = new ArrayList<>();
         for (Component line : description) {
            old_lore.add(Message.getInstance(line).toString());
         }
         meta.setLore(old_lore);
      } else {
         meta.lore(description);
      }
      return this;
   }

   public ItemFactory model(int customModelData) {
      meta.setCustomModelData(customModelData);
      return this;
   }

   public ItemFactory unbreakable() {
      return unbreakable(true);
   }

   public ItemFactory unbreakable(boolean value) {
      meta.setUnbreakable(value);
      return this;
   }

   public ItemFactory flags(ItemFlag... flags) {
      meta.addItemFlags(flags);
      return this;
   }

   public ItemFactory damage(double damage) {
      return setAttribute(Attribute.GENERIC_ATTACK_DAMAGE, damage);
   }

   public ItemFactory health(double health) {
      return setAttribute(Attribute.GENERIC_MAX_HEALTH, health);
   }

   public ItemFactory knockbackResistance(double knockback) {
      return setAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockback);
   }

   public ItemFactory moveSpeed(double speed) {
      return setAttribute(Attribute.GENERIC_MOVEMENT_SPEED, speed);
   }

   public ItemFactory attackSpeed(double speed) {
      return setAttribute(Attribute.GENERIC_ATTACK_SPEED, speed);
   }

   public ItemFactory attackKnockback(double knockback) {
      return setAttribute(Attribute.GENERIC_ATTACK_SPEED, knockback);
   }

   public ItemFactory setAttribute(Attribute attribute, double value) {
      meta.removeAttributeModifier(attribute);
      meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.name(), value - 1, AttributeModifier.Operation.ADD_NUMBER, item.getType().getEquipmentSlot()));
      return this;
   }

   public ItemFactory multiplyAttribute(Attribute attribute, double value) {
      meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.name(), value, AttributeModifier.Operation.MULTIPLY_SCALAR_1, item.getType().getEquipmentSlot()));
      return this;
   }

   protected ItemStack generateItem() {
      item.setItemMeta(meta);
      return item;
   }

   public ItemFactory enchantment(Enchantment enchantment, int level) {
      meta.addEnchant(enchantment, level, true);
      return this;
   }

   public boolean isPotionMeta() {
      return meta instanceof PotionMeta;
   }

   public ItemFactory customEffect(PotionEffect effect) {
      if (isPotionMeta()) {
         ((PotionMeta) meta).addCustomEffect(effect, false);
      }
      return this;
   }

   public ItemFactory customEffect(PotionEffectType effect, int duration, int level) {
      return this.customEffect(new PotionEffect(effect, duration, level));
   }

   public boolean isSkullMeta() {
      return meta instanceof SkullMeta;
   }

   public ItemFactory playerProfile(PlayerProfile profile) {
      if (isSkullMeta()) {
         ((SkullMeta) meta).setPlayerProfile(profile);
      }
      return this;
   }

   public ItemStack build() {
      return this.generateItem();
   }
}
