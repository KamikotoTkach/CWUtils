package ru.cwcode.cwutils.colors;

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ColorUtils {
  private static final List<String> colors = List.of("0123456789abcdef".split(""));
  
  /**
   * Не работает с новыми версиями Adventure (с тех пор, как удалили TextColor.lerp)
   */
  @NotNull
  public static TextColor lerpManyColors(int i, float sectionLen, TextColor... colors) {
    return TextColor.lerp((i % sectionLen) / sectionLen, colors[(int) Math.floor(i / sectionLen)], colors[(int) Math.ceil(i / sectionLen)]);
  }
  
  /**
   * Не работает с новыми версиями Adventure (с тех пор, как удалили TextColor.lerp)
   */
  public static TextColor lerpManyColors(float lerp, TextColor... colors) {
    var sectionLen = 255.0 * (colors.length - 1);
    int i = (int) ((sectionLen * (colors.length - 1)) * lerp);
    return TextColor.lerp((float) ((i % sectionLen) / sectionLen), colors[(int) Math.floor(i / sectionLen)], colors[(int) Math.ceil(i / sectionLen)]);
  }
  
  public static boolean isColor(String color) {
    return colors.contains(color);
  }
  
  public static List<String> getColors() {
    return colors;
  }
}
