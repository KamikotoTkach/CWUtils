package ru.cwcode.cwutils.confirmable;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;

public class ConfirmAPI {
  static ConcurrentHashMap<CommandSender, ConfirmRequest> requests = new ConcurrentHashMap<>();
  
  public static ConfirmableBuilder requestBuilder(CommandSender sender, String confirmableString, long timeToConfirm) {
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
    Bukkit.getScheduler().runTask(confirmRequest.plugin, confirmRequest.onSuccess);
    requests.remove(player);
  }
  
  public static class ConfirmableBuilder {
    ConfirmRequest request;
    
    public ConfirmableBuilder(CommandSender sender, String confirmableString, long ticksToRequest) {
      request = new ConfirmRequest(sender, confirmableString, ticksToRequest);
    }
    
    public ConfirmableBuilder success(Runnable success) {
      request.onSuccess = success;
      return this;
    }
    
    public ConfirmableBuilder expired(Runnable expired) {
      request.onExpired = expired;
      return this;
    }
    
    public void register(JavaPlugin plugin) {
      requests.put(request.sender, request);
      request.startTimer(plugin);
      
      if (!ChatOutListener.IS_REGISTERED) {
        Bukkit.getPluginManager().registerEvents(new ChatOutListener(), plugin);
        ChatOutListener.IS_REGISTERED = true;
      }
    }
  }
}
