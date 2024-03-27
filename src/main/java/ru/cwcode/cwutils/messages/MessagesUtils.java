package ru.cwcode.cwutils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import ru.cwcode.cwutils.server.ServerUtils;

public class MessagesUtils {
  public static void send(CommandSender sender, Component message) {
    if (ServerUtils.isVersionBefore1_16_5()) {
      sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(message));
      return;
    }
    
    sender.sendMessage(message);
  }
  
  public static void send(CommandSender sender, String message) {
    send(sender, LegacyComponentSerializer.legacySection().deserialize(message));
  }
}
