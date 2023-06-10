package tkachgeek.tkachutils.items.activeItem;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActiveItemRegistry {
  static HashMap<JavaPlugin, PluginEntry> activeItems = new HashMap<>();
  
  public static void register(ActiveItem activeItem) {
    if (!activeItems.containsKey(activeItem.getPlugin())) {
      activeItems.put(activeItem.getPlugin(), new PluginEntry(activeItem.getPlugin()));
    }
    
    activeItems.get(activeItem.getPlugin()).addItem(activeItem);
  }
  
  public static void unregister(JavaPlugin plugin) {
    activeItems.remove(plugin);
  }
  
  public static void perform(JavaPlugin plugin, ItemActionEvent event) {
    if (activeItems.containsKey(plugin)) {
      activeItems.get(plugin).handleAction(event);
    }
  }
  
  private static class PluginEntry {
    List<ActiveItem> items = new ArrayList<>();
    ActiveItemListener listener;
    
    public PluginEntry(JavaPlugin plugin) {
      this.listener = new ActiveItemListener(plugin);
      Bukkit.getPluginManager().registerEvents(listener, plugin);
    }
    
    public void unregister() {
      items.clear();
      listener = null;
    }
    
    public void addItem(ActiveItem item) {
      items.add(item);
    }
    
    public boolean handlesAction(ItemAction action) {
      for (ActiveItem x : items) {
        if (x.isBinded(action)) {
          return true;
        }
      }
      return false;
    }
    
    public void handleAction(ItemActionEvent event) {
      for (ActiveItem x : items) {
        x.perform(event);
      }
    }
  }
}
