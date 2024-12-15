package ru.cwcode.cwutils.dynamicBossBar.personal.v2;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import ru.cwcode.cwutils.server.PaperServerUtils;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class PersonalBossBarBuilder {
  protected UUID uuid = UUID.randomUUID();
  protected Function<Player, Component> title = player -> Component.empty();
  protected Function<Player, Float> progress = player -> 1f;
  protected Function<Player, Boolean> shouldDisplay = player -> true;
  protected Function<Player, BossBar.Color> color = player -> BossBar.Color.WHITE;
  protected Function<Player, BossBar.Overlay> overlay = player -> BossBar.Overlay.NOTCHED_12;
  protected Supplier<Boolean> shouldRemove = () -> false;
  
  public PersonalBossBarBuilder setUuid(UUID uuid) {
    this.uuid = uuid;
    return this;
  }
  
  public PersonalBossBarBuilder setTitle(Function<Player, Component> title) {
    this.title = title;
    return this;
  }
  
  public PersonalBossBarBuilder setProgress(Function<Player, Float> progress) {
    this.progress = progress;
    return this;
  }
  
  public PersonalBossBarBuilder setShouldDisplay(Function<Player, Boolean> shouldDisplay) {
    this.shouldDisplay = shouldDisplay;
    return this;
  }
  
  public PersonalBossBarBuilder setColor(Function<Player, BossBar.Color> color) {
    this.color = color;
    return this;
  }
  
  public PersonalBossBarBuilder setOverlay(Function<Player, BossBar.Overlay> overlay) {
    this.overlay = overlay;
    return this;
  }
  
  public PersonalBossBarBuilder setShouldRemove(Supplier<Boolean> shouldRemove) {
    this.shouldRemove = shouldRemove;
    return this;
  }
  
  public PersonalBossBar build() {
    return PaperServerUtils.isVersionGreater_1_16_5() ?
      new PersonalBossBar(uuid, title, progress, shouldDisplay, color, overlay, shouldRemove)
      : new PersonalBossBar_v1_16_5(uuid, title, progress, shouldDisplay, color, overlay, shouldRemove);
  }
}