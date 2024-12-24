package ru.cwcode.cwutils.items.rechargable;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.cwcode.cwutils.datetime.TimeFormatter;

import java.time.Duration;

public interface RechargeableItem {
  long getRecharge(NamespacedKey key);
  
  default long getRecharge() {
    return getRecharge(getRechargeKey());
  }
  
  NamespacedKey getRechargeKey(String key);
  
  default NamespacedKey getRechargeKey() {
    return getRechargeKey("");
  }
  
  default long getTimestamp(NamespacedKey rechargeKey, ItemStack itemStack) {
    return itemStack.getItemMeta().getPersistentDataContainer().getOrDefault(
       rechargeKey, PersistentDataType.LONG, 0L
    );
  }
  
  default long getTimestamp(ItemStack itemStack) {
    NamespacedKey rechargeKey = getRechargeKey();
    return getTimestamp(rechargeKey, itemStack);
  }
  
  default long getRechargeExpire(NamespacedKey rechargeKey, ItemStack itemStack) {
    return getTimestamp(rechargeKey, itemStack) + getRecharge(rechargeKey) * 1000L;
  }
  
  default long getRechargeExpire(ItemStack itemStack) {
    NamespacedKey rechargeKey = getRechargeKey();
    return getRechargeExpire(rechargeKey, itemStack);
  }
  
  default boolean isInRecharge(NamespacedKey rechargeKey, ItemStack itemStack) {
    long rechargeExpire = getRechargeExpire(rechargeKey, itemStack);
    return rechargeExpire > System.currentTimeMillis();
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
  
  default long getRechargeRemain(NamespacedKey rechargeKey, ItemStack itemStack) {
    long expire = getRechargeExpire(rechargeKey, itemStack);
    return Math.max(expire - System.currentTimeMillis(), 0);
  }
  
  default long getRechargeRemain(ItemStack itemStack) {
    NamespacedKey rechargeKey = getRechargeKey();
    return getRechargeRemain(rechargeKey, itemStack);
  }
  
  default String getRechargeRemainTime(Duration duration) {
    return TimeFormatter.getFormattedTime(duration);
  }
  
  default String getRechargeRemainTime(NamespacedKey rechargeKey, ItemStack itemStack) {
    long rechargeRemain = getRechargeRemain(rechargeKey, itemStack);
    return getRechargeRemainTime(Duration.ofMillis(rechargeRemain));
  }
  
  default String getRechargeRemainTime(ItemStack itemStack) {
    NamespacedKey rechargeKey = getRechargeKey();
    return getRechargeRemainTime(rechargeKey, itemStack);
  }
}
