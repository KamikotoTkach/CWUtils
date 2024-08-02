package ru.cwcode.cwutils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.jetbrains.annotations.NotNull;

public class ReloadCatcher implements Listener {
  
  private static void sendWarning() {
    Bukkit.getLogger().severe(warningMessage());
  }
  
  private static void sendWarning(CommandSender sender) {
    sender.sendMessage(warningMessage());
  }
  
  private static @NotNull String warningMessage() {
    return "Restarting a Minecraft server with the /reload confirm command can be dangerous," +
           " as it reloads all plugins and configuration files without completely rebooting the server." +
           " This can lead to: Loss of data or progress;" +
           " Plugins not working correctly;" +
           " Possible crashes and errors.";
  }
  
  @EventHandler
  void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
    if (event.getMessage().equals("/reload confirm")) {
      event.setCancelled(true);
      sendWarning(event.getPlayer());
    }
  }
  
  @EventHandler
  void onServerReload(ServerLoadEvent event) {
    if (event.getType() == ServerLoadEvent.LoadType.RELOAD) {
      sendWarning();
      Bukkit.getServer().shutdown();
    }
  }
}
