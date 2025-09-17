package ru.cwcode.cwutils.bossBar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class LegacyBossBarAdapter {
  private static final HashMap<UUID, BossBar> cachedBossBars = new HashMap<>();
  
  private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
                                                                                                        .hexColors()
                                                                                                        .useUnusualXRepeatedCharacterHexFormat()
                                                                                                        .character(LegacyComponentSerializer.SECTION_CHAR)
                                                                                                        .build();
  
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
    
    BarStyle barStyle = switch (overlay) {
      case NOTCHED_6 -> BarStyle.SEGMENTED_6;
      case NOTCHED_10 -> BarStyle.SEGMENTED_10;
      case NOTCHED_12 -> BarStyle.SEGMENTED_12;
      case NOTCHED_20 -> BarStyle.SEGMENTED_20;
      default -> BarStyle.SOLID;
    };
    
    BarColor barColor = BarColor.valueOf(color.name());
    
    Set<BarFlag> barFlags = flags.stream().map(flag -> switch (flag) {
      case PLAY_BOSS_MUSIC -> BarFlag.PLAY_BOSS_MUSIC;
      case DARKEN_SCREEN -> BarFlag.DARKEN_SKY;
      case CREATE_WORLD_FOG -> BarFlag.CREATE_FOG;
    }).collect(Collectors.toSet());
    
    org.bukkit.boss.BossBar bossBarLegacy = cachedBossBars.get(uuid);
    if (bossBarLegacy == null) {
      bossBarLegacy = createBossBar(uuid, barColor, barStyle, barFlags.toArray(BarFlag[]::new));
    }
    
    bossBarLegacy.setProgress(progress);
    bossBarLegacy.setStyle(barStyle);
    bossBarLegacy.setTitle(LEGACY_COMPONENT_SERIALIZER.serialize(name));
    bossBarLegacy.setColor(barColor);
    
    for (BarFlag value : BarFlag.values()) {
      boolean shouldBe = barFlags.contains(value);
      boolean actual = bossBarLegacy.hasFlag(value);
      
      if (shouldBe && !actual) {
        bossBarLegacy.addFlag(value);
        continue;
      }
      
      if (!shouldBe && actual) {
        bossBarLegacy.removeFlag(value);
      }
    }
    
    return bossBarLegacy;
  }
  
  public static @NotNull BossBar createBossBar(UUID uuid, BarColor barColor, BarStyle barStyle, BarFlag... barFlags) {
    BossBar bossBarLegacy = Bukkit.createBossBar("loading...", barColor, barStyle, barFlags);
    cachedBossBars.put(uuid, bossBarLegacy);
    return bossBarLegacy;
  }
}