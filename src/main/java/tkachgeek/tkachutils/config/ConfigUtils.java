package tkachgeek.tkachutils.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class ConfigUtils {
   public Component getComponent(String json) {
      Component component;
      if (json.isEmpty()) {
         component = Component.text("Предмет");
      } else {
         try {
            if (json.matches("^\\{.*}$")) {
               component = GsonComponentSerializer.gson().deserialize(json);
            } else {
               component = LegacyComponentSerializer.legacyAmpersand().deserialize(json.replace("§", "&"));
            }
         } catch (Exception exception) {
            component = Component.text(json).style(Style.style(TextColor.color(255, 255, 255)));
         }
      }
      return component.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
   }

   public Component[] getComponents(List<String> json) {
      Component[] components;
      if (json.isEmpty()) {
         components = new Component[1];
         components[0] = Component.text("Предмет").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
         ;
      } else {
         components = new Component[json.size()];
         for (int i = 0; i < json.size(); i++) {
            try {
               if (json.get(i).matches("^\\{.*}$")) {
                  components[i] = GsonComponentSerializer.gson().deserialize(json.get(i)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
                  ;
               } else {
                  components[i] = LegacyComponentSerializer.legacyAmpersand().deserialize(json.get(i).replace("§", "&")).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
                  ;
               }
            } catch (Exception exception) {
               components[i] = Component.text(json.get(i)).style(Style.style(TextColor.color(255, 255, 255))).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
               ;
            }
         }
      }
      return components;
   }
}
