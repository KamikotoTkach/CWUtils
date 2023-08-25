package tkachgeek.tkachutils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;

class Placeholder {
   private enum PlaceholderSymbols {
      left("<"),
      right(">");

      private final String symbol;

      PlaceholderSymbols(String symbol) {
         this.symbol = symbol;
      }

      public String getSymbol() {
         return this.symbol;
      }
   }

   private static Placeholder instance;

   private Message message;

   private Placeholder() {
   }

   protected static Placeholder getInstance(Message message) {
      if (instance == null) instance = new Placeholder();
      instance.message = message.clone();
      return instance;
   }

   private String formatPlaceholder(String placeholder) {
      if (placeholder.startsWith(PlaceholderSymbols.left.getSymbol())
            && placeholder.endsWith(PlaceholderSymbols.right.getSymbol())) {
         return placeholder;
      }

      return PlaceholderSymbols.left.getSymbol() + placeholder + PlaceholderSymbols.right.getSymbol();
   }

   Message replacePlaceholders(String placeholder, Component value) {
      placeholder = this.formatPlaceholder(placeholder);
      TextReplacementConfig config = TextReplacementConfig.builder()
                                                          .matchLiteral(placeholder)
                                                          .replacement(value)
                                                          .build();

      this.message.message = (TextComponent) this.message.message.replaceText(config);
      return this.message;
   }
}
