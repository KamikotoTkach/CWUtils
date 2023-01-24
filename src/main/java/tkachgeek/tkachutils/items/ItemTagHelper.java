package tkachgeek.tkachutils.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class ItemTagHelper {
  JavaPlugin plugin;
  
  public ItemTagHelper(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  //INTEGER
  
  public boolean hasIntTag(ItemMeta meta, String key) {
    return hasIntTag(meta, new NamespacedKey(plugin, key));
  }
  
  public void set(ItemMeta meta, String key, int value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public Integer get(ItemMeta meta, String key, int defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public boolean hasIntTag(ItemMeta meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
  }
  
  public void set(ItemMeta meta, NamespacedKey key, int value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
  }
  
  public int get(ItemMeta meta, NamespacedKey key, int defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, defaultValue);
  }
  
  public void incrementInt(ItemMeta meta, String key) {
    incrementInt(meta, new NamespacedKey(plugin, key));
  }
  
  public void decrementInt(ItemMeta meta, String key) {
    decrementInt(meta, new NamespacedKey(plugin, key));
  }
  
  public void incrementInt(ItemMeta meta, NamespacedKey key) {
    addIntToIntTag(meta, key, 1);
  }
  
  public void decrementInt(ItemMeta meta, NamespacedKey key) {
    addIntToIntTag(meta, key, -1);
  }
  
  public void addIntToIntTag(ItemMeta meta, String key, int value) {
    addIntToIntTag(meta, new NamespacedKey(plugin, key), value);
  }
  
  public void addIntToIntTag(ItemMeta meta, NamespacedKey key, int value) {
    if (!hasIntTag(meta, key)) {
      set(meta, key, value);
    } else {
      set(meta, key, value + get(meta, key, 0));
    }
  }
  
  //STRING
  
  public boolean hasStringTag(ItemMeta meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  
  public void set(ItemMeta meta, String key, String value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public String get(ItemMeta meta, String key, String defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public boolean hasStringTag(ItemMeta meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
  }
  
  public void set(ItemMeta meta, NamespacedKey key, String value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
  }
  
  public String get(ItemMeta meta, NamespacedKey key, String defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, defaultValue);
  }
  
  //DOUBLE
  
  public boolean hasDoubleTag(ItemMeta meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  
  public void set(ItemMeta meta, String key, Double value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public Double get(ItemMeta meta, String key, Double defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public boolean hasDoubleTag(ItemMeta meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.DOUBLE);
  }
  
  public void set(ItemMeta meta, NamespacedKey key, Double value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
  }
  
  public Double get(ItemMeta meta, NamespacedKey key, Double defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, defaultValue);
  }
  //BYTE ARRAY
  
  public boolean hasByteArrTag(ItemMeta meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  
  public void set(ItemMeta meta, String key, byte[] value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public byte[] get(ItemMeta meta, String key, byte[] defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public boolean hasByteArrTag(ItemMeta meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE_ARRAY);
  }
  
  public void set(ItemMeta meta, NamespacedKey key, byte[] value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);
  }
  
  public byte[] get(ItemMeta meta, NamespacedKey key, byte[] defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BYTE_ARRAY, defaultValue);
  }
  
  public Set<NamespacedKey> getItemMetaKeys(ItemMeta meta) {
    return meta.getPersistentDataContainer().getKeys();
  }
}
