package tkachgeek.tkachutils.dynamicBossBar.broadcast;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tkachgeek.tkachutils.bossBar.LegacyBossBarAdapter;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class BroadcastBossBar_v1_16_5 extends BroadcastBossBar {
  BroadcastBossBar_v1_16_5(@NotNull UUID uuid, @NotNull Supplier<Component> title, @NotNull Supplier<Float> progress, @NotNull Supplier<Boolean> shouldRemove, @NotNull Function<Player, Boolean> shouldDisplay, @NotNull Supplier<BossBar.Color> color, @NotNull Supplier<BossBar.Overlay> overlay, @NotNull Supplier<Collection<UUID>> viewers) {
    super(uuid, title, progress, shouldRemove, shouldDisplay, color, overlay, viewers);
  }
  
  @Override
  public void hide(Player player) {
    org.bukkit.boss.BossBar bossBar = LegacyBossBarAdapter.get(getUUID());
    if (bossBar == null) return;
    
    bossBar.removePlayer(player);
  }
  
  @Override
  public void show(Player player) {
    org.bukkit.boss.BossBar bossBar = LegacyBossBarAdapter.get(getUUID());
    if (bossBar == null) return;
    
    bossBar.addPlayer(player);
  }
  
  @Override
  public void onRemove() {
    super.onRemove();
    
    LegacyBossBarAdapter.remove(getUUID());
  }
  
  @Override
  public void update() {
    super.update();
    
    LegacyBossBarAdapter.merge(getUUID(), getBossBar());
  }
}
