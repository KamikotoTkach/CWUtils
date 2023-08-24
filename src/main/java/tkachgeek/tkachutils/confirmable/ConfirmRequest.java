package tkachgeek.tkachutils.confirmable;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class ConfirmRequest {
   public Plugin plugin;
   CommandSender sender;
   String required;
   long ticksToExpire;
   BukkitTask expiredTask = null;
   Runnable onSuccess = null;
   Runnable onExpired = null;

   /**
    * @param sender         отправитель
    * @param required       сообщение которое нужно отправить
    * @param millisToExpire время действия (в миллисекундах)
    */
   public ConfirmRequest(CommandSender sender, String required, long millisToExpire) {
      this.sender = sender;
      this.required = required;
      this.ticksToExpire = millisToExpire / 50;
   }

   public void startTimer(JavaPlugin plugin) {
      expiredTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
         if (onExpired != null) onExpired.run();
         ConfirmAPI.requests.remove(sender, this);
      }, ticksToExpire);
      this.plugin = plugin;
   }

   public void stopTimer() {
      expiredTask.cancel();
   }
}
