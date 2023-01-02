package tkachgeek.tkachutils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class Builder {
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

    Builder(String message) {
        this.message = message;
    }

    private String formatPlaceholder(String placeholder) {
        if (placeholder.startsWith(PlaceholderSymbols.left.getSymbol())
                && placeholder.endsWith(PlaceholderSymbols.right.getSymbol())) {
            return placeholder;
        }

        return PlaceholderSymbols.left.getSymbol() + placeholder + PlaceholderSymbols.right.getSymbol();
    }

    public Builder replacePlaceholders(String placeholder, Object value) {
        placeholder = formatPlaceholder(placeholder);
        this.message = this.message.replaceAll(placeholder, String.valueOf(value));
        return this;
    }

    public Builder replacePlaceholders(String placeholder, Component value) {
        return replacePlaceholders(placeholder, LegacyComponentSerializer.legacySection().serialize(value).replaceAll("\\\\", ""));
    }

    public Message getMessage() {
        return new Message(message);
    }
}
