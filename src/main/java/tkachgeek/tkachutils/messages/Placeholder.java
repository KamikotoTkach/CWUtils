package tkachgeek.tkachutils.messages;

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

   private String message;

   Placeholder(String message) {
      this.message = message;
   }

   private String formatPlaceholder(String placeholder) {
      if (placeholder.startsWith(PlaceholderSymbols.left.getSymbol())
            && placeholder.endsWith(PlaceholderSymbols.right.getSymbol())) {
         return placeholder;
      }

      return PlaceholderSymbols.left.getSymbol() + placeholder + PlaceholderSymbols.right.getSymbol();
   }

   Message replacePlaceholders(String placeholder, String value) {
      placeholder = formatPlaceholder(placeholder);
      this.message = this.message.replaceAll(placeholder, value);
      return Message.getInstance(this.message);
   }
}
