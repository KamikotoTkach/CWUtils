package tkachgeek.tkachutils.text.component.miniMessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class LegacyComponentUtil {
  public static Component[] parse(String[] text) {
    Component[] components = new Component[text.length];
    for (int i = 0; i < text.length; i++) {
      components[i] = parse(text[i]);
    }
    return components;
  }
  
  public static Component[] parse(List<String> text) {
    Component[] components = new Component[text.size()];
    for (int i = 0; i < text.size(); i++) {
      components[i] = parse(text.get(i));
    }
    return components;
  }
  
  public static Component parse(String text) {
    return LegacyComponentSerializer.builder().character('&').hexCharacter('#').build().deserialize(text);
  }
}
