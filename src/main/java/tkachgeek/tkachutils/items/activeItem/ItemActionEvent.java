package tkachgeek.tkachutils.items.activeItem;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemActionEvent {
  Player player;
  ItemStack itemStack;
  Cancellable cancellable;
  ItemAction action;
  EquipmentSlot hand = null;
  
  public ItemActionEvent(Player player, ItemStack itemStack, ItemAction action, Cancellable cancellable, EquipmentSlot hand) {
    this.player = player;
    this.itemStack = itemStack;
    this.cancellable = cancellable;
    this.action = action;
    this.hand = hand;
  }
  
  public ItemActionEvent(Player player, ItemStack itemStack, ItemAction action, Cancellable cancellable) {
    this.player = player;
    this.itemStack = itemStack;
    this.cancellable = cancellable;
    this.action = action;
  }
  
  public @Nullable EquipmentSlot getHand() {
    return hand;
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public ItemStack getItemStack() {
    return itemStack;
  }
  
  public Cancellable getCancellable() {
    return cancellable;
  }
  
  public ItemAction getAction() {
    return action;
  }
  
  public void setCancelled(boolean cancelled) {
    cancellable.setCancelled(cancelled);
  }
}
