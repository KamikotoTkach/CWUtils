package tkachgeek.tkachutils.messages;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Collection;
import java.util.Map;

public abstract class Message {
   protected TextComponent message;

   public Message(TextComponent message) {
      this.message = message;
   }

   public Message(String message) {
      this(Message.from(message));
   }

   public Message placeholder(String placeholder, TextComponent value) {
      return Placeholder.getInstance(this).replacePlaceholders(placeholder, value);
   }

   public Message placeholder(String placeholder, String value) {
      return this.placeholder(placeholder, Message.from(value));
   }

   public Message placeholder(String placeholder, Object value) {
      if (value instanceof TextComponent) {
         return this.placeholder(placeholder, (TextComponent) value);
      }

      return this.placeholder(placeholder, String.valueOf(value));
   }

   public Message placeholders(Map<String, String> placeholders) {
      Message message = this.clone();
      for (String placeholder : placeholders.keySet()) {
         message = message.placeholder(placeholder, placeholders.get(placeholder));
      }

      return message;
   }

   public Component get() {
      return this.message.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
   }

   public abstract void send(Audience player);

   public abstract void send(String name, Collection<? extends Audience> receivers);

   public void broadcast(Collection<? extends Audience> receivers) {
      for (Audience audience : receivers) {
         this.send(audience);
      }
   }

   public abstract void sendActionBar(Audience player);

   public abstract void sendActionBar(String name, Collection<? extends Audience> audiences);

   public void broadcastActionBar(Collection<? extends Audience> receivers) {
      for (Audience audience : receivers) {
         this.sendActionBar(audience);
      }
   }

   protected abstract Message clone();

   @Override
   public String toString() {
      return Message.from(this.message);
   }

   public static TextComponent from(String message) {
      return LegacyComponentSerializer.legacySection().deserialize(message);
   }

   public static String from(Component message) {
      return LegacyComponentSerializer.legacySection().serialize(message);
   }
}
