package ru.cwcode.cwutils.dynamicBossBar.personal;

import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cwcode.cwutils.numbers.NumbersUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@Getter
public class DynamicBossBar {
  private final UUID uuid;
  private final Supplier<Component> title;
  private final Supplier<Float> progress;
  private final Supplier<Boolean> shouldRemove;
  private final Supplier<Boolean> shouldDisplay;
  private final Supplier<BossBar.Color> color;
  private final Supplier<BossBar.Overlay> overlay;
  private final BossBar bossBar;
  
  DynamicBossBar(@NotNull UUID uuid,
                 @NotNull Supplier<Component> title,
                 @NotNull Supplier<Float> progress,
                 @NotNull Supplier<Boolean> shouldRemove,
                 @NotNull Supplier<Boolean> shouldDisplay,
                 @NotNull Supplier<BossBar.Color> color,
                 @NotNull Supplier<BossBar.Overlay> overlay) {
    
    this.uuid = uuid;
    this.title = title;
    this.progress = progress;
    this.shouldRemove = shouldRemove;
    this.shouldDisplay = shouldDisplay;
    this.color = color;
    this.overlay = overlay;
    this.bossBar = BossBar.bossBar(title.get(), progress.get(), color.get(), overlay.get());
  }
  
  public static DynamicBossBarBuilder of(Supplier<Component> title) {
    return new DynamicBossBarBuilder().setTitle(title);
  }
  
  public UUID getUUID() {
    return uuid;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    DynamicBossBar that = (DynamicBossBar) o;
    
    return uuid.equals(that.uuid);
  }
  
  public void hide(Player player) {
    player.hideBossBar(bossBar);
  }
  
  public void show(Player player) {
    player.showBossBar(bossBar);
  }
  
  public void onRemove() {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      hide(onlinePlayer);
    }
  }
  
  public void update(Player onlinePlayer) {
    bossBar.name(title.get());
    bossBar.color(color.get());
    bossBar.overlay(overlay.get());
    bossBar.progress((float) NumbersUtils.bound(progress.get(), 0, 1));
    
    if (shouldDisplay.get()) {
      show(onlinePlayer);
    } else {
      hide(onlinePlayer);
    }
  }
}
