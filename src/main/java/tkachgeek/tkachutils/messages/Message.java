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

   private Message placeholder(String placeholder, String value) {
      return new Placeholder(this.message).replacePlaceholders(placeholder, value);
   }

   public Message placeholder(String placeholder, Object value) {
      return this.placeholder(placeholder, String.valueOf(value));
   }

   public Message placeholder(String placeholder, Component value) {
      return this.placeholder(placeholder, LegacyComponentSerializer.legacySection().serialize(value).replaceAll("\\\\", ""));
   }

   public Component get() {
      return LegacyComponentSerializer.legacySection().deserialize(this.message);
   }

   public void send(CommandSender sender) {
      if (ServerUtils.isVersionBefore1_16_5()) {
         sender.sendMessage(this.message);
         return;
      }

      sender.sendMessage(this::get);
   }
}
