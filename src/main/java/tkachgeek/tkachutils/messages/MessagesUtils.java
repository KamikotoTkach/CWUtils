package tkachgeek.tkachutils.messages;

import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import tkachgeek.tkachutils.server.ServerUtils;

public class MessagesUtils {
    public static void send(CommandSender sender, Component message) {
        if(ServerUtils.isVersionBefore1_16_5()) {
            sender.sendMessage(BukkitComponentSerializer.legacy().serialize(message));
            return;
        }

        sender.sendMessage(message);
    }

    public static void send(CommandSender sender, String message) {
        send(sender, BukkitComponentSerializer.legacy().deserialize(message));
    }
}
