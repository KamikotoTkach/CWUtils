package ru.cwcode.cwutils.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.cwcode.cwutils.datetime.TimeFormatter;

import java.time.Duration;

public interface Rechargeable {
  long getRecharge(NamespacedKey key);
  
  default long getRecharge() {
    return getRecharge(getRechargeKey());
  }
  
  NamespacedKey getRechargeKey(String key);
  
  default NamespacedKey getRechargeKey() {
    return getRechargeKey("");
  }
  
  default long getExpire(NamespacedKey rechargeKey, ItemStack itemStack) {
    return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(
       rechargeKey, PersistentDataType.LONG, 0L
    );
  }
  
  default long getExpire(ItemStack itemStack) {
    NamespacedKey rechargeKey = getRechargeKey();
    return getExpire(rechargeKey, itemStack);
  }
  
  default boolean isInRecharge(NamespacedKey rechargeKey, ItemStack itemStack) {
    long coolDown = getExpire(rechargeKey, itemStack) + getRecharge(rechargeKey) * 1000L;
    return coolDown > System.currentTimeMillis();
  }
  
  default boolean isInRecharge(ItemStack itemStack) {
    NamespacedKey rechargeKey = getRechargeKey();
    return isInRecharge(rechargeKey, itemStack);
  }
  
  default void setRecharge(NamespacedKey rechargeKey, ItemStack itemStack) {
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (itemMeta == null) return;
    
    itemMeta.getPersistentDataContainer().set(
       rechargeKey,
       PersistentDataType.LONG,
       System.currentTimeMillis()
    );
    
    itemStack.setItemMeta(itemMeta);
  }
  
  default void setRecharge(ItemStack itemStack) {
    NamespacedKey rechargeKey = getRechargeKey();
    setRecharge(rechargeKey, itemStack);
  }
  
  default String getRechargeTime(Duration duration) {
    return TimeFormatter.getFormattedTime(duration);
  }
  
  default String getRechargeTime(NamespacedKey rechargeKey, ItemStack itemStack) {
    long expire = getExpire(rechargeKey, itemStack);
    long ms = Math.max(expire - System.currentTimeMillis(), 0);
    
    return getRechargeTime(Duration.ofMillis(ms));
  }
  
  default String getRechargeTime(ItemStack itemStack) {
    NamespacedKey rechargeKey = getRechargeKey();
    return getRechargeTime(rechargeKey, itemStack);
  }
}
