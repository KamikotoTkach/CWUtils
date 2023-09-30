package tkachgeek.tkachutils.collections.expired;

import com.velocitypowered.api.proxy.ProxyServer;

import java.time.Duration;

public class VelocityRunnableExpiredSet<Element> extends RunnableExpiredSet<Element> {
   private final ProxyServer server;
   private final Object plugin;

   public VelocityRunnableExpiredSet(ProxyServer server, Object plugin) {
      this.server = server;
      this.plugin = plugin;
   }

   @Override
   protected void register(Element element, Runnable runnable, Duration duration) {
      this.server.getScheduler()
                 .buildTask(this.plugin, this.getAction(element, runnable))
                 .delay(duration)
                 .schedule();
   }
}
