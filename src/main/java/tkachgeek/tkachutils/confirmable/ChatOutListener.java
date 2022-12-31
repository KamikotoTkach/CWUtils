package tkachgeek.tkachutils.confirmable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatOutListener implements Listener {
    public static boolean IS_REGISTERED = false;

    @EventHandler
    void onChatOut(AsyncPlayerChatEvent event) {
        if (ConfirmAPI.senderAffected(event.getPlayer())) {
            if (ConfirmAPI.getString(event.getPlayer()).equals((event.getMessage()))) {
                ConfirmAPI.onSuccess(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }
}
