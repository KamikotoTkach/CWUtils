package ru.cwcode.cwutils.items.activeItem;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActiveItemBuilder {
  HashMap<ItemAction, Consumer<ItemActionEvent>> actions = new HashMap<>();
  Predicate<ItemStack> predicate;
  
  public ActiveItemBuilder predicate(Predicate<ItemStack> predicate) {
    this.predicate = predicate;
    return this;
  }
  
  public ActiveItemBuilder bind(ItemAction action, Consumer<ItemActionEvent> function) {
    actions.put(action, function);
    return this;
  }
  
  public ActiveItemBuilder bind(List<ItemAction> bindActions, Consumer<ItemActionEvent> function) {
    for (ItemAction action : bindActions) {
      actions.put(action, function);
    }
    return this;
  }
  
  public void register(JavaPlugin plugin) {
    ActiveItemRegistry.register(new ActiveItem(predicate, actions, plugin));
  }
}
