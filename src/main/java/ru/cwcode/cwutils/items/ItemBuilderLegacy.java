package ru.cwcode.cwutils.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.cwcode.cwutils.messages.PaperMessage;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilderLegacy extends ItemBuilder {
  public ItemBuilderLegacy(Material material) {
    super(material);
  }
  
  public ItemBuilderLegacy(ItemStack item) {
    super(item);
  }
  
  @Override
  public List<Component> description() {
    if (!this.hasDescription()) {
      return new ArrayList<>();
    }
    
    List<Component> description = new ArrayList<>();
    for (String line : meta.getLore()) {
      description.add(PaperMessage.getInstance(line).get());
    }
    return description;
  }
  
  @Override
  public ItemBuilder description(List<Component> description) {
    List<String> lore = new ArrayList<>();
    for (Component line : description) {
      lore.add(PaperMessage.getInstance(line).toString());
    }
    meta.setLore(lore);
    return this;
  }
  
  @Override
  public Component name() {
    if (!this.hasName()) {
      return Component.translatable(this.item.getType().getTranslationKey())
                      .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
    
    return PaperMessage.getInstance(meta.getDisplayName()).get();
  }
  
  @Override
  public ItemBuilder name(Component name) {
    meta.setDisplayName(PaperMessage.getInstance(name).toString());
    return this;
  }
}
