package tkachgeek.tkachutils.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
  
  public ItemFactory name(Component name) {
    meta.displayName(name);
    return this;
  }
  
  public ItemFactory description(Component... description) {
    meta.lore(Arrays.asList(description));
    return this;
  }
  public ItemFactory description(List<Component> description) {
    meta.lore(description);
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
  
  public ItemStack build() {
    return generateItem();
  }
}
