package ru.cwcode.cwutils.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Collections;
import java.util.Map;

public interface Craftable {
  ItemStack getResult();
  
  String[] getShape();
  
  Map<Character, String> getRecipe();
  
  NamespacedKey getCraftableKey();
  
  default Map<String, ItemStack> getCustomIngredients() {
    return Collections.emptyMap();
  }
  
  default void register() {
    Map<Character, String> recipe = this.getRecipe();
    if (recipe.isEmpty()) return;
    
    String[] shape = this.getShape();
    if (shape.length == 0) return;
    
    NamespacedKey namespacedKey = getCraftableKey();
    if (namespacedKey == null) return;
    
    ItemStack result = getResult();
    if (result == null || result.getType().isAir()) return;
    
    ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, result);
    shapedRecipe.shape(shape);
    
    Map<String, ItemStack> ingredients = this.getCustomIngredients();
    
    for (char character : recipe.keySet()) {
      String ingredient = recipe.get(character);
      
      ItemStack itemStack = ingredients.get(ingredient);
      if (itemStack != null && !itemStack.getType().isAir()) {
        shapedRecipe.setIngredient(character, itemStack.clone());
        continue;
      }
      
      Material material = Material.matchMaterial(ingredient);
      if (material != null && !material.isAir() && material.isItem()) {
        shapedRecipe.setIngredient(character, material);
        continue;
      }
      
      Bukkit.getConsoleSender().sendMessage("Can't register craft for custom item " + this.getClass().getSimpleName());
      Bukkit.getConsoleSender().sendMessage("Ingredient " + ingredient + " is not custom item or material");
      return;
    }
    
    Bukkit.addRecipe(shapedRecipe);
  }
  
  default void unregister() {
    NamespacedKey namespacedKey = getCraftableKey();
    if (namespacedKey == null) return;
    
    Bukkit.removeRecipe(namespacedKey);
  }
}
