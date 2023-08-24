package tkachgeek.tkachutils.confirmable.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.concurrent.ConcurrentHashMap;

public class ConfirmAPI {
   static ConcurrentHashMap<Player, ConfirmRequest> requests = new ConcurrentHashMap<>();

   public static ConfirmableBuilder requestBuilder(Player sender, String confirmableString, long timeToConfirm) {
      return new ConfirmableBuilder(sender, confirmableString, timeToConfirm);
   }

   public static String getString(Player player) {
      return requests.get(player).required;
   }

   public static boolean senderAffected(Player player) {
      return requests.containsKey(player);
   }

   public static void onSuccess(Player player) {
      ConfirmRequest confirmRequest = requests.get(player);
      confirmRequest.stopTimer();
      confirmRequest.onSuccess.run();
      requests.remove(player);
   }

   public static class ConfirmableBuilder {
      ConfirmRequest request;

      public ConfirmableBuilder(Player sender, String confirmableString, long timeToRequest) {
         request = new ConfirmRequest(sender, confirmableString, timeToRequest);
      }

      public ConfirmableBuilder success(Runnable success) {
         request.onSuccess = success;
         return this;
      }

      public ConfirmableBuilder expired(Runnable expired) {
         request.onExpired = expired;
         return this;
      }

      public void register(ProxyServer server, Object plugin) {
         requests.put(request.sender, request);
         request.startTimer(plugin);

         if (!ChatOutListener.IS_REGISTERED) {
            server.getEventManager().register(plugin, new ChatOutListener());
            ChatOutListener.IS_REGISTERED = true;
         }
      }
   }
}
