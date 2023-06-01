package tkachgeek.tkachutils.items.activeItem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.tkachutils.collections.EnumUtils;

import java.util.Optional;

public class ActiveItemListener implements Listener {
  JavaPlugin plugin;
  
  public ActiveItemListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  public JavaPlugin getPlugin() {
    return plugin;
  }
  
  @EventHandler
  void onInteract(PlayerInteractEvent event) {
    if (event.getItem() == null || event.getItem().getType().isAir()) return;
    
    Optional<ItemAction> action = EnumUtils.getEnumInstance(ItemAction.values(), event.getAction().name());
    if (action.isEmpty()) return;
    
    ActiveItemRegistry.perform(plugin, new ItemActionEvent(event.getPlayer(), event.getItem(), action.get(), event));
  }
  
  @EventHandler
  void onInventoryClick(InventoryClickEvent event) {
    if (event.getCurrentItem() == null || event.getCurrentItem().getType().isAir()) return;
    Optional<ItemAction> action = EnumUtils.getEnumInstance(ItemAction.values(), event.getAction().name());
    if (action.isEmpty()) return;
    
    ActiveItemRegistry.perform(plugin, new ItemActionEvent((Player) event.getWhoClicked(), event.getCurrentItem(), action.get(), event));
    
    action = EnumUtils.getEnumInstance(ItemAction.values(), event.getClick().name());
    if (action.isEmpty()) return;
    
    ActiveItemRegistry.perform(plugin, new ItemActionEvent((Player) event.getWhoClicked(), event.getCurrentItem(), action.get(), event));
  }
}
