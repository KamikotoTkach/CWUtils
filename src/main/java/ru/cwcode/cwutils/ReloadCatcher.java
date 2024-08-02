package ru.cwcode.cwutils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ReloadCatcher implements Listener {
  @EventHandler
  void onServerReload(ServerLoadEvent event) {
    if (event.getType() == ServerLoadEvent.LoadType.RELOAD) {
      Bukkit.getLogger().severe("Restarting a Minecraft server with the /reload confirm command can be dangerous," +
                                " as it reloads all plugins and configuration files without completely rebooting the server." +
                                " This can lead to: Loss of data or progress;" +
                                " Plugins not working correctly;" +
                                " Possible crashes and errors.");
      Bukkit.getServer().shutdown();
    }
  }
}
