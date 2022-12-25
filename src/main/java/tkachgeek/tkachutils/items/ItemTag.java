package tkachgeek.tkachutils.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class ItemTag {
  JavaPlugin plugin;
  
  public ItemTag(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  public static ItemTag instance(JavaPlugin plugin) {
    return new ItemTag(plugin);
  }
  
  public boolean hasIntTag(ItemMeta meta, String key) {
    try {
      return meta.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.INTEGER);
    } catch (Exception ignored) {
    }
    return false;
  }
  
  public boolean hasStringTag(ItemMeta meta, String key) {
    try {
      return meta.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.STRING);
    } catch (Exception ignored) {
    }
    return false;
  }
  
  public boolean hasDoubleItemTag(ItemMeta meta, String key) {
    try {
      return meta.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE);
    } catch (Exception ignored) {
    }
    return false;
  }
  
  public void addIntToIntTag(ItemMeta meta, String key, int value) {
    if (!hasIntTag(meta, key)) {
      setItemTag(meta, key, value);
    } else {
      setItemTag(meta, key, value + getItemTag(meta, key, 0));
    }
  }
  
  public void setItemTag(ItemMeta meta, String key, String value) {
    try {
    meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
    } catch (Exception ignored) {
    }
  }
  
  public void setItemTag(ItemMeta meta, String key, int value) {
    try {
      meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.INTEGER, value);
    } catch (Exception ignored) {
    
    }
  }
  
  public void setItemTag(ItemMeta meta, String key, double value) {
    try {
      meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE, value);
    } catch (Exception ignored) {
    
    }
  }
  
  public String getItemTag(ItemMeta meta, String key, String defaultValue) {
    if (meta == null) return defaultValue;
    if (meta.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.STRING))
      return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
    return defaultValue;
  }
  
  public int getItemTag(ItemMeta meta, String key, int defaultValue) {
    if (meta == null) return defaultValue;
    if (meta.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.INTEGER))
      return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.INTEGER);
    return defaultValue;
  }
  
  public double getItemTag(ItemMeta meta, String key, double defaultValue) {
    if (meta == null) return defaultValue;
    if (meta.getPersistentDataContainer().has(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE))
      return meta.getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.DOUBLE);
    return defaultValue;
  }
  
  public Set<NamespacedKey> getItemMetaKeys(ItemMeta meta) {
    return meta.getPersistentDataContainer().getKeys();
  }
}
