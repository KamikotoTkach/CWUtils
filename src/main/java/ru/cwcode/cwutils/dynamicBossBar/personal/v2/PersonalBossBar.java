package ru.cwcode.cwutils.dynamicBossBar.personal.v2;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import ru.cwcode.cwutils.numbers.NumbersUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PersonalBossBar {
  protected final UUID uuid;
  protected final Function<Player, Component> title;
  protected final Function<Player, Float> progress;
  protected final Function<Player, Boolean> shouldDisplay;
  protected final Function<Player, BossBar.Color> color;
  protected final Function<Player, BossBar.Overlay> overlay;
  protected final Supplier<Boolean> shouldRemove;
  
  Map<Player, BossBar> bossBars = new ConcurrentHashMap<>();
  
  protected void delete() {
    bossBars.forEach(this::hide);
    bossBars = new ConcurrentHashMap<>();
  }
  
  protected void remove(Player player) {
    BossBar bar = bossBars.remove(player);
    if (bar != null) {
      hide(player, bar);
    }
  }
  
  protected boolean shouldRemove() {
    return shouldRemove.get();
  }
  
  protected void update(Player player) {
    if (!player.isOnline()) {
      return;
    }
    
    BossBar bossBar = bossBars.get(player);
    Boolean shouldDisplay = this.shouldDisplay.apply(player);
    
    if (bossBar == null && shouldDisplay) {
      bossBar = createBossBar();
      bossBars.put(player, bossBar);
    }
    
    if (bossBar == null) return;
    
    if (!shouldDisplay) {
      hide(player, bossBar);
      return;
    }
    
    bossBar.name(title.apply(player));
    bossBar.progress((float) NumbersUtils.bound(progress.apply(player), 0, 1));
    bossBar.color(color.apply(player));
    bossBar.overlay(overlay.apply(player));
    
    show(player, bossBar);
  }
  
  protected BossBar createBossBar() {
    return BossBar.bossBar(Component.empty(), 1, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_12);
  }
  
  protected void hide(Player player, BossBar bossBar) {
    player.hideBossBar(bossBar);
  }
  
  protected void show(Player player, BossBar bossBar) {
    player.showBossBar(bossBar);
  }
}
