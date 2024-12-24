package ru.cwcode.cwutils.items.rechargable;

import org.bukkit.entity.Player;
import ru.cwcode.cwutils.datetime.TimeFormatter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRecharge {
  private UUID player;
  private final Map<String, Long> timestamps = new HashMap<>();
  
  private PlayerRecharge() {
  }
  
  public PlayerRecharge(UUID player) {
    this.player = player;
  }
  
  public PlayerRecharge(Player player) {
    this(player.getUniqueId());
  }
  
  public UUID getPlayer() {
    return player;
  }
  
  public long getTimestamp(String rechargeKey) {
    return timestamps.getOrDefault(rechargeKey, 0L);
  }
  
  public long getRechargeExpire(String rechargeKey, long recharge) {
    return getTimestamp(rechargeKey) + recharge * 1000L;
  }
  
  public boolean isInRecharge(String rechargeKey, long recharge) {
    long rechargeExpire = getRechargeExpire(rechargeKey, recharge);
    return rechargeExpire > System.currentTimeMillis();
  }
  
  public void setRecharge(String rechargeKey) {
    timestamps.put(rechargeKey, System.currentTimeMillis());
  }
  
  public long getRechargeRemain(String rechargeKey, long recharge) {
    long expire = getRechargeExpire(rechargeKey, recharge);
    return Math.max(expire - System.currentTimeMillis(), 0);
  }
  
  public String getRechargeRemainTime(Duration duration) {
    return TimeFormatter.getFormattedTime(duration);
  }
  
  public String getRechargeRemainTime(String rechargeKey, long recharge) {
    long rechargeRemain = getRechargeRemain(rechargeKey, recharge);
    return getRechargeRemainTime(Duration.ofMillis(rechargeRemain));
  }
}
