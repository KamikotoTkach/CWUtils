package tkachgeek.tkachutils.colors;

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

public class ColorUtils {
  @NotNull
  public static TextColor lerpManyColors(int i, float sectionLen, TextColor... colors) {
    return TextColor.lerp((i % sectionLen) / sectionLen, colors[(int) Math.floor(i / sectionLen)], colors[(int) Math.ceil(i / sectionLen)]);
  }
  
  public static TextColor lerpManyColors(float lerp, TextColor... colors) {
    var sectionLen = 255.0 * (colors.length - 1);
    int i = (int) ((sectionLen * (colors.length - 1)) * lerp);
    return TextColor.lerp((float) ((i % sectionLen) / sectionLen), colors[(int) Math.floor(i / sectionLen)], colors[(int) Math.ceil(i / sectionLen)]);
  }
}
