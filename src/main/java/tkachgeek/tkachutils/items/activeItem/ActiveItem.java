package tkachgeek.tkachutils.items.activeItem;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActiveItem {
  JavaPlugin plugin;
  HashMap<ItemAction, Consumer<ItemActionEvent>> functions;
  Predicate<ItemStack> predicate;
  
  public ActiveItem(Predicate<ItemStack> predicate, HashMap<ItemAction, Consumer<ItemActionEvent>> functions, JavaPlugin plugin) {
    this.plugin = plugin;
    this.functions = functions;
    this.predicate = predicate;
  }
  
  public JavaPlugin getPlugin() {
    return plugin;
  }
  
  public void perform(ItemActionEvent event) {
    if (isBinded(event.getAction()) && predicate.test(event.itemStack)) {
      functions.get(event.getAction()).accept(event);
    }
  }
  
  public boolean isBinded(ItemAction action) {
    return functions.containsKey(action);
  }
}
