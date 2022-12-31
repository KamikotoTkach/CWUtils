package tkachgeek.tkachutils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import tkachgeek.tkachutils.server.ServerUtils;

public class Message {
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

    public Message(String message) {
        this.message = message;
    }

    public Message(Component message) {
        this(LegacyComponentSerializer.legacySection().serialize(message).replaceAll("\\\\", ""));
    }

    private String formatPlaceholder(String placeholder) {
        if(placeholder.startsWith(PlaceholderSymbols.left.getSymbol())
                && placeholder.endsWith(PlaceholderSymbols.right.getSymbol())) {
            return placeholder;
        }

        return PlaceholderSymbols.left.getSymbol() + placeholder + PlaceholderSymbols.right.getSymbol();
    }

    public Message replacePlaceholders(String placeholder, Object value) {
        placeholder = formatPlaceholder(placeholder);
        this.message = this.message.replaceAll(placeholder, String.valueOf(value));
        return this;
    }

    public Message replacePlaceholders(String placeholder, Component value) {
        return replacePlaceholders(placeholder, LegacyComponentSerializer.legacySection().serialize(value).replaceAll("\\\\", ""));
    }

    public Component get() {
        return LegacyComponentSerializer.legacySection().deserialize(message);
    }

    public void send(CommandSender sender) {
        if(ServerUtils.isVersionBefore1_16_5()) {
            sender.sendMessage(message);
            return;
        }

        sender.sendMessage(this::get);
    }
}
