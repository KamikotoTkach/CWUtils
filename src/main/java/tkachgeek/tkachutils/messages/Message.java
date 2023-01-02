package tkachgeek.tkachutils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import tkachgeek.tkachutils.server.ServerUtils;

public class Message {
    private final String message;

    public Message(String message) {
        this.message = message;
    }

    public Message(Component message) {
        this(LegacyComponentSerializer.legacySection().serialize(message));
    }

    public Builder builder() {
        return new Builder(this.message);
    }

    public Component get() {
        return LegacyComponentSerializer.legacySection().deserialize(message);
    }

    public void send(CommandSender sender) {
        if (ServerUtils.isVersionBefore1_16_5()) {
            sender.sendMessage(message);
            return;
        }

        sender.sendMessage(this::get);
    }
}
