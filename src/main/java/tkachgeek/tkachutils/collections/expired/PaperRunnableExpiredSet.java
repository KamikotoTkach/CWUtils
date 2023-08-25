package tkachgeek.tkachutils.collections.expired;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class PaperRunnableExpiredSet<Element> extends RunnableExpiredSet<Element> {
   private final JavaPlugin plugin;
   private final boolean isAsync;

   public PaperRunnableExpiredSet(JavaPlugin plugin, boolean isAsync) {
      this.plugin = plugin;
      this.isAsync = isAsync;
   }

   public PaperRunnableExpiredSet(JavaPlugin plugin) {
      this(plugin, false);
   }

   @Override
   protected void register(Element element, Runnable runnable, Duration duration) {
      long delay = duration.toMillis() / 50;
      if (this.isAsync) {
         Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, this.getAction(element, runnable), delay);
      } else {
         Bukkit.getScheduler().runTaskLater(this.plugin, this.getAction(element, runnable), delay);
      }
   }
}
