package tkachgeek.tkachutils.text.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import tkachgeek.tkachutils.collections.CollectionUtils;

public class ComponentRandomColor {
  public static Component of(String text, TextColor... colors) {
    Component component = Component.empty();
    if (colors.length == 0) {
      for (int i = 0; i < text.length(); i++) {
        component = component.append(Component.text(text.charAt(i)).color(TextColor.color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
      }
      return component;
    }
    for (int i = 0; i < text.length(); i++) {
      component = component.append(Component.text(text.charAt(i)).color(CollectionUtils.getRandomArrayEntry(colors)));
    }
    return component;
  }
}
