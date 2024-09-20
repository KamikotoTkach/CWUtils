package ru.cwcode.cwutils.craftable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Map;

public interface Craftable {
  String getName();
  
  ItemStack getResult();
  
  String[] getShape();
  
  Map<Character, String> getRecipe();
  
  default Map<String, ItemStack> getCustomIngredients() {
    return Collections.emptyMap();
  }
  
  default NamespacedKey getNamespacedKey(JavaPlugin plugin) {
    return new NamespacedKey(plugin, Craftable.getKeyPrefix() + "_" + this.getName());
  }
  
  default void register(JavaPlugin plugin) {
    Map<Character, String> recipe = this.getRecipe();
    if (recipe.isEmpty()) return;
    
    String[] shape = this.getShape();
    if (shape.length == 0) return;
    
    NamespacedKey namespacedKey = this.getNamespacedKey(plugin);
    if (namespacedKey == null) return;
    
    ShapedRecipe shapedRecipe = new ShapedRecipe(
       namespacedKey,
       this.getResult()
    );
    
    shapedRecipe.shape(shape);
    
    Map<String, ItemStack> ingredients = this.getCustomIngredients();
    
    for (char character : recipe.keySet()) {
      String ingredient = recipe.get(character);
      
      ItemStack itemStack = ingredients.get(ingredient);
      if (itemStack != null) {
        shapedRecipe.setIngredient(character, itemStack.clone());
        continue;
      }
      
      Material material = Material.matchMaterial(ingredient);
      if (material != null && material.isItem()) {
        shapedRecipe.setIngredient(character, material);
        continue;
      }
      
      Bukkit.getConsoleSender().sendMessage("Can't register craft for custom item " + this.getName());
      Bukkit.getConsoleSender().sendMessage("Ingredient " + ingredient + " is not custom item or material");
      return;
    }
    
    Bukkit.addRecipe(shapedRecipe);
  }
  
  static String getKeyPrefix() {
    return "craftable";
  }
}
