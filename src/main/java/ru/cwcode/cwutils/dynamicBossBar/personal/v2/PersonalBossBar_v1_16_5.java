package ru.cwcode.cwutils.dynamicBossBar.personal.v2;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import ru.cwcode.cwutils.bossBar.LegacyBossBarAdapter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class PersonalBossBar_v1_16_5 extends PersonalBossBar {
  
  Map<BossBar, UUID> bars = new ConcurrentHashMap<>();
  
  public PersonalBossBar_v1_16_5(UUID uuid, Function<Player, Component> title, Function<Player, Float> progress, Function<Player, Boolean> shouldDisplay, Function<Player, BossBar.Color> color, Function<Player, BossBar.Overlay> overlay, Supplier<Boolean> shouldRemove) {
    super(uuid, title, progress, shouldDisplay, color, overlay, shouldRemove);
  }
  
  @Override
  protected void delete() {
    super.delete();
    
    bars.values().forEach(LegacyBossBarAdapter::remove);
  }
  
  @Override
  protected void remove(Player player) {
    BossBar bossBar = bossBars.get(player);
    
    LegacyBossBarAdapter.remove(bars.get(bossBar));
    bars.remove(bossBar);
    
    super.remove(player);
  }
  
  @Override
  protected void hide(Player player, BossBar bossBar) {
    UUID barUUID = bars.get(bossBar);
    if (barUUID == null) return;
    
    org.bukkit.boss.BossBar bossBarOld = LegacyBossBarAdapter.get(barUUID);
    if (bossBar == null) return;
    
    bossBarOld.removePlayer(player);
  }
  
  @Override
  protected void show(Player player, BossBar bossBar) {
    org.bukkit.boss.BossBar bossBarOld = LegacyBossBarAdapter.get(bars.get(bossBar));
    if (bossBar == null) return;
    
    bossBarOld.addPlayer(player);
  }
  
  @Override
  protected BossBar createBossBar() {
    BossBar bossBar = super.createBossBar();
    
    UUID uuid = UUID.randomUUID();
    bars.put(bossBar, uuid);
    LegacyBossBarAdapter.createBossBar(uuid, BarColor.WHITE, BarStyle.SEGMENTED_12);
    
    return bossBar;
  }
  
  @Override
  protected void update(Player player) {
    super.update(player);
    
    BossBar bossBar = bossBars.get(player);
    
    if (bossBar == null) return;
    LegacyBossBarAdapter.merge(bars.get(bossBar), bossBar);
  }
}
