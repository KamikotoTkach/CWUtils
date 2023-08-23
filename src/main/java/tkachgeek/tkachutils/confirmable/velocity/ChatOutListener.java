package tkachgeek.tkachutils.confirmable.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;

public class ChatOutListener {
   public static boolean IS_REGISTERED = false;

   @Subscribe
   void onChatOut(PlayerChatEvent event) {
      if (ConfirmAPI.senderAffected(event.getPlayer())) {
         if (ConfirmAPI.getString(event.getPlayer()).equals(event.getMessage())) {
            ConfirmAPI.onSuccess(event.getPlayer());
            event.setResult(PlayerChatEvent.ChatResult.denied());
         }
      }
   }
}
