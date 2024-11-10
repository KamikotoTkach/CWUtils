package ru.cwcode.cwutils.text.component;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import ru.cwcode.cwutils.colors.ColorUtils;

/**
 * Не работает с новыми версиями Adventure (с тех пор, как удалили TextColor.lerp)
 */
public class ComponentGradient {
  
  public static Component of(String text, TextColor... colors) {
    if (colors.length == 0) {
      return Component.text(text);
    }
    
    if (colors.length == 1) {
      return Component.text(text).color(colors[0]);
    }
    
    Component component = Component.empty();
    
    if (colors.length == 2) {
      for (int i = 0; i < text.length(); i++) {
        component = component.append(Component.text(text.charAt(i)).color(TextColor.lerp((i + 1f) / text.length(), colors[0], colors[1])));
      }
      return component;
    }
    
    float sectionLen = text.length() / (0f + colors.length - 1);
    
    for (int i = 0; i < text.length(); i++) {
      component = component.append(Component.text(text.charAt(i))
                                            .color(ColorUtils.lerpManyColors(i, sectionLen, colors)));
    }
    return component;
  }
}
