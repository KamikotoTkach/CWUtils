package ru.cwcode.cwutils.items.activeItem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.cwutils.collections.EnumUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class ActiveItemListener implements Listener {
  JavaPlugin plugin;
  HashSet<UUID> dropsToCancelInteractEventOMGwhyImDoingThat = new HashSet<>();
  
  public ActiveItemListener(JavaPlugin plugin) {
    this.plugin = plugin;
  }
  
  public JavaPlugin getPlugin() {
    return plugin;
  }
  
  @EventHandler()
  void onInteract(PlayerInteractEvent event) {
    if (dropsToCancelInteractEventOMGwhyImDoingThat.remove(event.getPlayer().getUniqueId())) return;
    
    if (event.getItem() == null || event.getItem().getType().isAir()) return;
    
    Optional<ItemAction> action = EnumUtils.getEnumInstance(ItemAction.values(), event.getAction().name());
    if (action.isEmpty()) return;
    
    ActiveItemRegistry.perform(plugin, new ItemActionEvent(event.getPlayer(), event.getItem(), action.get(), event, event.getHand()));
  }
  
  @EventHandler()
  void onDropItem(PlayerDropItemEvent event) {
    dropsToCancelInteractEventOMGwhyImDoingThat.add(event.getPlayer().getUniqueId());
    
    ActiveItemRegistry.perform(plugin, new ItemActionEvent(event.getPlayer(), event.getItemDrop().getItemStack(), ItemAction.DROP_ITEM_EVENT, event));
  }
  
  @EventHandler()
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
