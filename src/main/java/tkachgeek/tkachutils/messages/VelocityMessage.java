package tkachgeek.tkachutils.messages;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.Collection;

public class VelocityMessage extends Message {
   private static VelocityMessage instance;

   public static VelocityMessage getInstance(TextComponent message) {
      if (instance == null) {
         instance = new VelocityMessage(message);
      }

      instance.set(message);
      return instance;
   }

   public static VelocityMessage getInstance(String message) {
      return VelocityMessage.getInstance(Message.from(message));
   }

   public static VelocityMessage getInstance(Component component) {
      if (component instanceof TextComponent) {
         return VelocityMessage.getInstance((TextComponent) component);
      } else {
         return VelocityMessage.getInstance(Message.from(component));
      }
   }

   private VelocityMessage() {
      this("empty");
   }

   public VelocityMessage(String message) {
      super(message);
   }

   public VelocityMessage(TextComponent message) {
      super(message);
   }

   @Override
   public void send(Audience player) {
      player.sendMessage(this::get);
   }

   @Override
   public void send(String name, Collection<? extends Audience> receivers) {
      for (Audience audience : receivers) {
         if (audience instanceof Player) {
            Player player = (Player) audience;
            if (player.getUsername().equals(name)) {
               this.send(player);
               return;
            }
         }
      }
   }

   @Override
   public void sendActionBar(Audience player) {
      player.sendActionBar(this::get);
   }

   @Override
   public void sendActionBar(String name, Collection<? extends Audience> audiences) {
      for (Audience audience : audiences) {
         if (audience instanceof Player) {
            Player player = (Player) audiences;
            if (player.getUsername().equals(name)) {
               this.sendActionBar(player);
               return;
            }
         }
      }
   }

   @Override
   protected Message clone() {
      return new VelocityMessage(this.get());
   }
}
