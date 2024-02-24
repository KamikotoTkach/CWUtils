package tkachgeek.tkachutils.dynamicBossBar.broadcast;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tkachgeek.tkachutils.server.ServerUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class BroadcastBossBarBuilder {
  private UUID uuid = UUID.randomUUID();
  private Supplier<Component> title;
  private Supplier<Float> progress = () -> 1.0f;
  private Supplier<Boolean> shouldRemove;
  private Function<Player, Boolean> shouldDisplay = (player) -> true;
  private Supplier<BossBar.Color> color = () -> BossBar.Color.WHITE;
  private Supplier<BossBar.Overlay> overlay = () -> BossBar.Overlay.PROGRESS;
  private Supplier<Collection<UUID>> viewers = Collections::emptySet;
  
  public BroadcastBossBarBuilder setUuid(@NotNull UUID uuid) {
    this.uuid = uuid;
    return this;
  }
  
  public BroadcastBossBarBuilder setViewers(Supplier<Collection<UUID>> viewers) {
    this.viewers = viewers;
    return this;
  }
  
  public BroadcastBossBarBuilder setTitle(@NotNull Supplier<Component> title) {
    this.title = title;
    return this;
  }
  
  public BroadcastBossBarBuilder setProgress(@NotNull Supplier<Float> progress) {
    this.progress = progress;
    return this;
  }
  
  public BroadcastBossBarBuilder setShouldRemove(@NotNull Supplier<Boolean> shouldRemove) {
    this.shouldRemove = shouldRemove;
    return this;
  }
  
  public BroadcastBossBarBuilder setShouldDisplay(@NotNull Function<Player, Boolean> shouldDisplay) {
    this.shouldDisplay = shouldDisplay;
    return this;
  }
  
  public BroadcastBossBarBuilder setColor(@NotNull Supplier<BossBar.Color> color) {
    this.color = color;
    return this;
  }
  
  public BroadcastBossBarBuilder setOverlay(@NotNull Supplier<BossBar.Overlay> overlay) {
    this.overlay = overlay;
    return this;
  }
  
  public BroadcastBossBar build() {
    return ServerUtils.isVersionGreater_1_16_5() ? //todo: может быть нужно с более высокой, не проверял
       new BroadcastBossBar(uuid, title, progress, shouldRemove, shouldDisplay, color, overlay, viewers)
       : new BroadcastBossBar_v1_16_5(uuid, title, progress, shouldRemove, shouldDisplay, color, overlay, viewers);
  }
}