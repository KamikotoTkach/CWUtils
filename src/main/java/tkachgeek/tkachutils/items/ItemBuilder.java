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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {
  
  protected ItemStack item;
  protected ItemMeta meta;
  
  public ItemBuilder(Material material) {
    item = new ItemStack(material);
    meta = item.getItemMeta();
  }
  
  public ItemBuilder(ItemStack item) {
    this.item = item;
    meta = item.getItemMeta();
  }
  
  static public ItemBuilder of(Material material) {
    return new ItemBuilder(material);
  }
  
  static public ItemBuilder of(ItemStack item) {
    return new ItemBuilder(item);
  }
  
  public ItemBuilder amount(int amount) {
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
    
    return meta.displayName();
  }
  
  public ItemBuilder name(Component name) {
    meta.displayName(name.decoration(TextDecoration.ITALIC, false));
    return this;
  }
  
  public boolean hasDescription() {
    return this.meta.hasLore();
  }
  
  public List<Component> description() {
    if (!this.hasDescription()) {
      return new ArrayList<>();
    }
    return meta.lore();
  }
  
  public ItemBuilder description(Component... description) {
    return this.description(Arrays.asList(description));
  }
  
  public ItemBuilder description(List<Component> description) {
    meta.lore(description.stream().map(x -> x.decoration(TextDecoration.ITALIC, false)).collect(Collectors.toList()));
    return this;
  }
  
  public ItemBuilder model(int customModelData) {
    meta.setCustomModelData(customModelData);
    return this;
  }
  
  public ItemBuilder unbreakable() {
    return unbreakable(true);
  }
  
  public ItemBuilder unbreakable(boolean value) {
    meta.setUnbreakable(value);
    return this;
  }
  
  public ItemBuilder flags(ItemFlag... flags) {
    meta.addItemFlags(flags);
    return this;
  }
  
  public ItemBuilder damage(double damage) {
    return setAttribute(Attribute.GENERIC_ATTACK_DAMAGE, damage);
  }
  
  public ItemBuilder health(double health) {
    return setAttribute(Attribute.GENERIC_MAX_HEALTH, health);
  }
  
  public ItemBuilder knockbackResistance(double knockback) {
    return setAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE, knockback);
  }
  
  public ItemBuilder moveSpeed(double speed) {
    return setAttribute(Attribute.GENERIC_MOVEMENT_SPEED, speed);
  }
  
  public ItemBuilder attackSpeed(double speed) {
    return setAttribute(Attribute.GENERIC_ATTACK_SPEED, speed);
  }
  
  public ItemBuilder attackKnockback(double knockback) {
    return setAttribute(Attribute.GENERIC_ATTACK_SPEED, knockback);
  }
  
  public ItemBuilder setAttribute(Attribute attribute, double value) {
    meta.removeAttributeModifier(attribute);
    meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.name(), value - 1, AttributeModifier.Operation.ADD_NUMBER, item.getType().getEquipmentSlot()));
    return this;
  }
  
  public ItemBuilder multiplyAttribute(Attribute attribute, double value) {
    meta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.name(), value, AttributeModifier.Operation.MULTIPLY_SCALAR_1, item.getType().getEquipmentSlot()));
    return this;
  }
  
  protected ItemStack generateItem() {
    item.setItemMeta(meta);
    return item;
  }
  
  public ItemBuilder enchantment(Enchantment enchantment, int level) {
    meta.addEnchant(enchantment, level, true);
    return this;
  }
  
  public boolean isPotionMeta() {
    return meta instanceof PotionMeta;
  }
  
  public ItemBuilder customEffect(PotionEffect effect) {
    if (isPotionMeta()) {
      ((PotionMeta) meta).addCustomEffect(effect, false);
    }
    return this;
  }
  
  public ItemBuilder customEffect(PotionEffectType effect, int duration, int level) {
    return this.customEffect(new PotionEffect(effect, duration, level));
  }
  
  public boolean isSkullMeta() {
    return meta instanceof SkullMeta;
  }
  
  public ItemBuilder playerProfile(PlayerProfile profile) {
    if (isSkullMeta()) {
      ((SkullMeta) meta).setPlayerProfile(profile);
    }
    return this;
  }
  
  public ItemStack build() {
    return this.generateItem();
  }
}
