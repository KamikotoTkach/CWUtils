package tkachgeek.tkachutils.persistent;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class PersistentHelper {
  JavaPlugin plugin;
  
  public PersistentHelper(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  public static Set<NamespacedKey> getPersistentDataHolderKeys(PersistentDataHolder holder) {
    return holder.getPersistentDataContainer().getKeys();
  }
  
  //INTEGER
  
  public boolean hasIntTag(PersistentDataHolder holder, String key) {
    return hasIntTag(holder, new NamespacedKey(plugin, key));
  }
  
  public static boolean hasIntTag(PersistentDataHolder holder, NamespacedKey key) {
    return holder.getPersistentDataContainer().has(key, PersistentDataType.INTEGER);
  }
  
  public void set(PersistentDataHolder holder, String key, int value) {
    set(holder, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(PersistentDataHolder holder, NamespacedKey key, int value) {
    holder.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
  }
  
  public void incrementInt(PersistentDataHolder holder, String key) {
    incrementInt(holder, new NamespacedKey(plugin, key));
  }
  
  public static void incrementInt(PersistentDataHolder holder, NamespacedKey key) {
    addIntToIntTag(holder, key, 1);
  }
  
  public static void addIntToIntTag(PersistentDataHolder holder, NamespacedKey key, int value) {
    if (!hasIntTag(holder, key)) {
      set(holder, key, value);
    } else {
      set(holder, key, value + get(holder, key, 0));
    }
  }
  
  public static int get(PersistentDataHolder holder, NamespacedKey key, int defaultValue) {
    if (holder == null) return defaultValue;
    return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, defaultValue);
  }
  
  public void decrementInt(PersistentDataHolder holder, String key) {
    decrementInt(holder, new NamespacedKey(plugin, key));
  }
  
  public int get(PersistentDataHolder holder, String key, int defaultValue) {
    return get(holder, new NamespacedKey(plugin, key), defaultValue);
  }
  public static void decrementInt(PersistentDataHolder holder, NamespacedKey key) {
    addIntToIntTag(holder, key, -1);
  }
  
  public void addIntToIntTag(PersistentDataHolder holder, String key, int value) {
    addIntToIntTag(holder, new NamespacedKey(plugin, key), value);
  }
  
  //STRING
  
  
  public boolean hasStringTag(PersistentDataHolder holder, String key) {
    return hasStringTag(holder, new NamespacedKey(plugin, key));
  }
  
  public static boolean hasStringTag(PersistentDataHolder holder, NamespacedKey key) {
    return holder.getPersistentDataContainer().has(key, PersistentDataType.STRING);
  }
  
  public void set(PersistentDataHolder holder, String key, String value) {
    set(holder, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(PersistentDataHolder holder, NamespacedKey key, String value) {
    holder.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
  }
  
  public String get(PersistentDataHolder holder, String key, String defaultValue) {
    return get(holder, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static String get(PersistentDataHolder holder, NamespacedKey key, String defaultValue) {
    if (holder == null) return defaultValue;
    return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, defaultValue);
  }
  
  //DOUBLE
  
  public boolean hasDoubleTag(PersistentDataHolder holder, String key) {
    return hasStringTag(holder, new NamespacedKey(plugin, key));
  }
  
  public void set(PersistentDataHolder holder, String key, double value) {
    set(holder, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(PersistentDataHolder holder, NamespacedKey key, double value) {
    holder.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
  }
  
  public double get(PersistentDataHolder holder, String key, double defaultValue) {
    return get(holder, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static double get(PersistentDataHolder holder, NamespacedKey key, double defaultValue) {
    if (holder == null) return defaultValue;
    return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, defaultValue);
  }
  
  //BYTE ARRAY
  
  public void set(PersistentDataHolder holder, String key, byte[] value) {
    set(holder, new NamespacedKey(plugin, key), value);
  }
  
  public static void set(PersistentDataHolder holder, NamespacedKey key, byte[] value) {
    holder.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);
  }
  
  public boolean hasByteArrTag(PersistentDataHolder holder, String key) {
    return hasStringTag(holder, new NamespacedKey(plugin, key));
  }
  
  public byte[] get(PersistentDataHolder holder, String key, byte[] defaultValue) {
    return get(holder, new NamespacedKey(plugin, key), defaultValue);
  }
  
  public static byte[] get(PersistentDataHolder holder, NamespacedKey key, byte[] defaultValue) {
    if (holder == null) return defaultValue;
    return holder.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BYTE_ARRAY, defaultValue);
  }
  
  public static boolean hasDoubleTag(PersistentDataHolder holder, NamespacedKey key) {
    return holder.getPersistentDataContainer().has(key, PersistentDataType.DOUBLE);
  }
  
  public static boolean hasByteArrTag(PersistentDataHolder holder, NamespacedKey key) {
    return holder.getPersistentDataContainer().has(key, PersistentDataType.BYTE_ARRAY);
  }

}
