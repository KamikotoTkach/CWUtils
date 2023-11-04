package tkachgeek.tkachutils.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * @deprecated Используйте <code>PersistentHelper</code, этот оставлен для совместимости
 */
@Deprecated(forRemoval = true)
public class ItemTagHelper {
  JavaPlugin plugin;
  
  public ItemTagHelper(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  //INTEGER
  
  public static boolean hasDoubleTag(ItemMeta meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.DOUBLE);
  }
  
  public static boolean hasByteArrTag(ItemMeta meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE_ARRAY);
  }
  
  public static Set<NamespacedKey> getItemMetaKeys(ItemMeta meta) {
    return meta.getPersistentDataContainer().getKeys();
  }
  
  public static boolean hasDoubleTag(PersistentDataHolder meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.DOUBLE);
  }
  
  public static boolean hasByteArrTag(PersistentDataHolder meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE_ARRAY);
  }
  
  public static Set<NamespacedKey> getPersistentDataHolderKeys(PersistentDataHolder meta) {
    return meta.getPersistentDataContainer().getKeys();
  }
  
  public boolean hasIntTag(ItemMeta meta, String key) {
    return hasIntTag(meta, new NamespacedKey(plugin, key));
  }
  
  public static boolean hasIntTag(ItemMeta meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
  }
  
  public void set(ItemMeta meta, String key, int value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(ItemMeta meta, NamespacedKey key, int value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
  }
  
  public void incrementInt(ItemMeta meta, String key) {
    incrementInt(meta, new NamespacedKey(plugin, key));
  }
  
  public static void incrementInt(ItemMeta meta, NamespacedKey key) {
    addIntToIntTag(meta, key, 1);
  }
  
  //STRING
  
  public static void addIntToIntTag(ItemMeta meta, NamespacedKey key, int value) {
    if (!hasIntTag(meta, key)) {
      set(meta, key, value);
    } else {
      set(meta, key, value + get(meta, key, 0));
    }
  }
  
  public static int get(ItemMeta meta, NamespacedKey key, int defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, defaultValue);
  }
  
  public void decrementInt(ItemMeta meta, String key) {
    decrementInt(meta, new NamespacedKey(plugin, key));
  }
  
  public static void decrementInt(ItemMeta meta, NamespacedKey key) {
    addIntToIntTag(meta, key, -1);
  }
  
  public void addIntToIntTag(ItemMeta meta, String key, int value) {
    addIntToIntTag(meta, new NamespacedKey(plugin, key), value);
  }
  
  public boolean hasStringTag(ItemMeta meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  
  //DOUBLE
  
  public static boolean hasStringTag(ItemMeta meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
  }
  
  public void set(ItemMeta meta, String key, String value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(ItemMeta meta, NamespacedKey key, String value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
  }
  
  public String get(ItemMeta meta, String key, String defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static String get(ItemMeta meta, NamespacedKey key, String defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, defaultValue);
  }
  
  public boolean hasDoubleTag(ItemMeta meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  //BYTE ARRAY
  
  public boolean hasByteArrTag(ItemMeta meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  
  public void set(ItemMeta meta, String key, byte[] value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(ItemMeta meta, NamespacedKey key, byte[] value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);
  }
  
  public byte[] get(ItemMeta meta, String key, byte[] defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static byte[] get(ItemMeta meta, NamespacedKey key, byte[] defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BYTE_ARRAY, defaultValue);
  }
  
  public int get(ItemMeta meta, String key, int defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public void set(ItemMeta meta, String key, double value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  //INTEGER
  
  public static void set(ItemMeta meta, NamespacedKey key, double value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
  }
  
  public double get(ItemMeta meta, String key, double defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static double get(ItemMeta meta, NamespacedKey key, double defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, defaultValue);
  }
  
  public boolean hasIntTag(PersistentDataHolder meta, String key) {
    return hasIntTag(meta, new NamespacedKey(plugin, key));
  }
  
  public static boolean hasIntTag(PersistentDataHolder meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
  }
  
  public void set(PersistentDataHolder meta, String key, int value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(PersistentDataHolder meta, NamespacedKey key, int value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
  }
  
  public void incrementInt(PersistentDataHolder meta, String key) {
    incrementInt(meta, new NamespacedKey(plugin, key));
  }
  
  public static void incrementInt(PersistentDataHolder meta, NamespacedKey key) {
    addIntToIntTag(meta, key, 1);
  }
  
  public static void addIntToIntTag(PersistentDataHolder meta, NamespacedKey key, int value) {
    if (!hasIntTag(meta, key)) {
      set(meta, key, value);
    } else {
      set(meta, key, value + get(meta, key, 0));
    }
  }
  
  public static int get(PersistentDataHolder meta, NamespacedKey key, int defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, defaultValue);
  }
  
  public void decrementInt(PersistentDataHolder meta, String key) {
    decrementInt(meta, new NamespacedKey(plugin, key));
  }
  
  //STRING
  
  public static void decrementInt(PersistentDataHolder meta, NamespacedKey key) {
    addIntToIntTag(meta, key, -1);
  }
  
  public void addIntToIntTag(PersistentDataHolder meta, String key, int value) {
    addIntToIntTag(meta, new NamespacedKey(plugin, key), value);
  }
  
  public boolean hasStringTag(PersistentDataHolder meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  
  public static boolean hasStringTag(PersistentDataHolder meta, NamespacedKey key) {
    return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
  }
  
  public void set(PersistentDataHolder meta, String key, String value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(PersistentDataHolder meta, NamespacedKey key, String value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
  }
  
  //DOUBLE
  
  public String get(PersistentDataHolder meta, String key, String defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static String get(PersistentDataHolder meta, NamespacedKey key, String defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, defaultValue);
  }
  
  public boolean hasDoubleTag(PersistentDataHolder meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  
  public boolean hasByteArrTag(PersistentDataHolder meta, String key) {
    return hasStringTag(meta, new NamespacedKey(plugin, key));
  }
  
  public void set(PersistentDataHolder meta, String key, byte[] value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(PersistentDataHolder meta, NamespacedKey key, byte[] value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);
  }
  //BYTE ARRAY
  
  public byte[] get(PersistentDataHolder meta, String key, byte[] defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static byte[] get(PersistentDataHolder meta, NamespacedKey key, byte[] defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BYTE_ARRAY, defaultValue);
  }
  
  public int get(PersistentDataHolder meta, String key, int defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public void set(PersistentDataHolder meta, String key, double value) {
    set(meta, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(PersistentDataHolder meta, NamespacedKey key, double value) {
    meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
  }
  
  public double get(PersistentDataHolder meta, String key, double defaultValue) {
    return get(meta, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static double get(PersistentDataHolder meta, NamespacedKey key, double defaultValue) {
    if (meta == null) return defaultValue;
    return meta.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, defaultValue);
  }
}
