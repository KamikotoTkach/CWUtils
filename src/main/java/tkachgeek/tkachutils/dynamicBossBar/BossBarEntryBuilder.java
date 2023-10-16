package tkachgeek.tkachutils.dynamicBossBar;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public class BossBarEntryBuilder {
  private UUID uuid = UUID.randomUUID();
  private Supplier<Component> title;
  private Supplier<Float> progress = () -> 1.0f;
  private Supplier<Boolean> shouldRemove;
  private Supplier<Boolean> shouldDisplay = () -> true;
  private Supplier<BossBar.Color> color = () -> BossBar.Color.WHITE;
  private Supplier<BossBar.Overlay> overlay = () -> BossBar.Overlay.PROGRESS;
  
  public BossBarEntryBuilder setUuid(@NotNull UUID uuid) {
    this.uuid = uuid;
    return this;
  }
  
  public BossBarEntryBuilder setTitle(@NotNull Supplier<Component> title) {
    this.title = title;
    return this;
  }
  
  public BossBarEntryBuilder setProgress(@NotNull Supplier<Float> progress) {
    this.progress = progress;
    return this;
  }
  
  public BossBarEntryBuilder setShouldRemove(@NotNull Supplier<Boolean> shouldRemove) {
    this.shouldRemove = shouldRemove;
    return this;
  }
  
  public BossBarEntryBuilder setShouldDisplay(@NotNull Supplier<Boolean> shouldDisplay) {
    this.shouldDisplay = shouldDisplay;
    return this;
  }
  
  public BossBarEntryBuilder setColor(@NotNull Supplier<BossBar.Color> color) {
    this.color = color;
    return this;
  }
  
  public BossBarEntryBuilder setOverlay(@NotNull Supplier<BossBar.Overlay> overlay) {
    this.overlay = overlay;
    return this;
  }
  
  public BossBarEntry build() {
    return new BossBarEntry(uuid, title, progress, shouldRemove, shouldDisplay, color, overlay);
  }
}