package tkachgeek.tkachutils.dynamicBossBar.personal;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tkachgeek.tkachutils.bossBar.LegacyBossBarAdapter;

import java.util.UUID;
import java.util.function.Supplier;

public class DynamicBossBar_v1_16_5 extends DynamicBossBar {
  DynamicBossBar_v1_16_5(@NotNull UUID uuid, @NotNull Supplier<Component> title, @NotNull Supplier<Float> progress, @NotNull Supplier<Boolean> shouldRemove, @NotNull Supplier<Boolean> shouldDisplay, @NotNull Supplier<BossBar.Color> color, @NotNull Supplier<BossBar.Overlay> overlay) {
    super(uuid, title, progress, shouldRemove, shouldDisplay, color, overlay);
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
}
