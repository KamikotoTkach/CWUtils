package ru.cwcode.cwutils.items.rechargable;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface RechargeablePlayer {
  long getRecharge(String key);
  
  String getRechargeKey(String key);
  
  PlayerRecharge getPlayerRecharge(UUID player);
  
  default PlayerRecharge getPlayerRecharge(Player player) {
    return getPlayerRecharge(player.getUniqueId());
  }
}
