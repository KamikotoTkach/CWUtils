package ru.cwcode.cwutils.bossBar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class LegacyBossBarAdapter {
  static HashMap<UUID, BossBar> cachedBossBars = new HashMap<>();
  
  public static void remove(UUID uuid) {
    cachedBossBars.remove(uuid);
  }
  
  public static BossBar get(UUID uuid) {
    return cachedBossBars.get(uuid);
  }
  
  public static BossBar merge(UUID uuid, net.kyori.adventure.bossbar.BossBar bossBar) {
    Component name = bossBar.name();
    float progress = bossBar.progress();
    
    Set<net.kyori.adventure.bossbar.BossBar.Flag> flags = bossBar.flags();
    net.kyori.adventure.bossbar.BossBar.Color color = bossBar.color();
    net.kyori.adventure.bossbar.BossBar.Overlay overlay = bossBar.overlay();
    
    BarStyle barStyle;
    switch (overlay) {
      case NOTCHED_6:
        barStyle = BarStyle.SEGMENTED_6;
        break;
      case NOTCHED_10:
        barStyle = BarStyle.SEGMENTED_10;
        break;
      case NOTCHED_12:
        barStyle = BarStyle.SEGMENTED_12;
        break;
      case NOTCHED_20:
        barStyle = BarStyle.SEGMENTED_20;
        break;
      default:
        barStyle = BarStyle.SOLID;
    }
    
    BarColor barColor = BarColor.valueOf(color.name());
    
    BarFlag[] barFlags = flags.stream().map(flag -> {
      switch (flag) {
        case PLAY_BOSS_MUSIC:
          return BarFlag.PLAY_BOSS_MUSIC;
        case DARKEN_SCREEN:
          return BarFlag.DARKEN_SKY;
        case CREATE_WORLD_FOG:
          return BarFlag.CREATE_FOG;
        default:
          return null;
      }
    }).distinct().toArray(BarFlag[]::new);
    
    org.bukkit.boss.BossBar bossBarLegacy = cachedBossBars.get(uuid);
    if (bossBarLegacy == null) {
      bossBarLegacy = Bukkit.createBossBar("loading...", barColor, barStyle, barFlags);
      cachedBossBars.put(uuid, bossBarLegacy);
    }
    
    bossBarLegacy.setProgress(progress);
    bossBarLegacy.setStyle(barStyle);
    bossBarLegacy.setTitle(LegacyComponentSerializer.legacySection().serialize(name));
    
    return bossBarLegacy;
  }
}

