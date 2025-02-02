package ru.cwcode.cwutils.dynamicBossBar.broadcast;

import lombok.Getter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cwcode.cwutils.numbers.NumbersUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class BroadcastBossBar {
  private final UUID uuid;
  @Getter
  private final Supplier<Component> title;
  private final Supplier<Float> progress;
  @Getter
  private final Supplier<Boolean> shouldRemove;
  @Getter
  private final Function<Player, Boolean> shouldDisplay;
  @Getter
  private final Supplier<BossBar.Color> color;
  @Getter
  private final Supplier<BossBar.Overlay> overlay;
  @Getter
  private final BossBar bossBar;
  private final Supplier<Collection<UUID>> viewers;
  private Collection<UUID> previousViewers = new ArrayList<>();
  
  BroadcastBossBar(
     @NotNull UUID uuid,
     @NotNull Supplier<Component> title,
     @NotNull Supplier<Float> progress,
     @NotNull Supplier<Boolean> shouldRemove,
     @NotNull Function<Player, Boolean> shouldDisplay,
     @NotNull Supplier<BossBar.Color> color,
     @NotNull Supplier<BossBar.Overlay> overlay,
     @NotNull Supplier<Collection<UUID>> viewers) {
    
    this.uuid = uuid;
    this.title = title;
    this.progress = progress;
    this.shouldRemove = shouldRemove;
    this.shouldDisplay = shouldDisplay;
    this.color = color;
    this.overlay = overlay;
    this.viewers = viewers;
    
    this.bossBar = BossBar.bossBar(title.get(), progress.get(), color.get(), overlay.get());
  }
  
  public static BroadcastBossBarBuilder of(Supplier<Component> title) {
    return new BroadcastBossBarBuilder().setTitle(title);
  }
  
  public UUID getUUID() {
    return uuid;
  }
  
  public float getProgress() {
    return (float) NumbersUtils.bound(progress.get(), 0, 1);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    
    BroadcastBossBar that = (BroadcastBossBar) o;
    
    return uuid.equals(that.uuid);
  }
  
  public void hide(Player player) {
    player.hideBossBar(bossBar);
  }
  
  public void onRemove() {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      hide(onlinePlayer);
    }
  }
  
  public void update() {
    bossBar.name(title.get());
    bossBar.color(color.get());
    bossBar.overlay(overlay.get());
    bossBar.progress(getProgress());
    
    Collection<UUID> newViewers = new ArrayList<>(viewers.get());
    
    previousViewers.removeAll(newViewers);
    previousViewers.stream()
                   .map(Bukkit::getPlayer)
                   .filter(Objects::nonNull)
                   .forEach(this::hide);
    
    newViewers.stream()
              .map(Bukkit::getPlayer)
              .filter(Objects::nonNull)
              .forEach(player -> {
                if (shouldDisplay.apply(player)) {
                  show(player);
                } else {
                  hide(player);
                }
              });
    
    previousViewers = newViewers;
  }
  
  public void show(Player player) {
    player.showBossBar(bossBar);
  }
}
