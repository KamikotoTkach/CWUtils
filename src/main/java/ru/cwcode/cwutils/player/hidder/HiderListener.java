package ru.cwcode.cwutils.player.hidder;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HiderListener implements Listener {
  private final PlayerHider playerHider;
  
  protected HiderListener(PlayerHider playerHider) {
    this.playerHider = playerHider;
  }
  
  @EventHandler
  void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    playerHider.updateHidden(player);
    playerHider.updateViewer(player);
  }
}
