package tkachgeek.tkachutils.confirmable;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatOutListener implements Listener {
    public static boolean IS_REGISTERED = false;
    
    @EventHandler(priority = EventPriority.LOWEST)
    void onChatOut(AsyncChatEvent event) {
        if (ConfirmAPI.senderAffected(event.getPlayer())) {
            if (ConfirmAPI.getString(event.getPlayer()).equals((((TextComponent) event.message()).content()))) {
                ConfirmAPI.onSuccess(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }
}
