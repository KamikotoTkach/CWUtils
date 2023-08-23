package tkachgeek.tkachutils.confirmable.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.time.Duration;

public class ConfirmRequest {
   public ProxyServer server;
   public Object plugin;
   Player sender;
   String required;
   Duration timeToExpire;
   ScheduledTask expiredTask = null;
   Runnable onSuccess = null;
   Runnable onExpired = null;

   public ConfirmRequest(Player sender, String required, Duration timeToExpire) {
      this.sender = sender;
      this.required = required;
      this.timeToExpire = timeToExpire;
   }

   public void startTimer(Object plugin) {
      expiredTask = server.getScheduler().buildTask(plugin, () -> {
         if (onExpired != null) onExpired.run();
         ConfirmAPI.requests.remove(sender, this);
      }).delay(timeToExpire).schedule();
      this.plugin = plugin;
   }

   public void stopTimer() {
      expiredTask.cancel();
   }
}
