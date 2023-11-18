package tkachgeek.tkachutils.dynamicBossBar.broadcast;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BroadcastBossBar {
  private final UUID uuid;
  private final Supplier<Component> title;
  private final Supplier<Float> progress;
  private final Supplier<Boolean> shouldRemove;
  private final Function<Player, Boolean> shouldDisplay;
  private final Supplier<BossBar.Color> color;
  private final Supplier<BossBar.Overlay> overlay;
  private final BossBar bossBar;
  private final Supplier<Collection<UUID>> viewers;
  private Collection<UUID> previousViewers = new ArrayList<>();
  
  BroadcastBossBar(UUID uuid, Supplier<Component> title, Supplier<Float> progress, Supplier<Boolean> shouldRemove, Function<Player, Boolean> shouldDisplay, Supplier<BossBar.Color> color, Supplier<BossBar.Overlay> overlay, Supplier<Collection<UUID>> viewers) {
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
  
  public Supplier<Component> getTitle() {
    return title;
  }
  
  public Supplier<Float> getProgress() {
    return progress;
  }
  
  public Supplier<Boolean> getShouldRemove() {
    return shouldRemove;
  }
  
  public Function<Player, Boolean> getShouldDisplay() {
    return shouldDisplay;
  }
  
  public Supplier<BossBar.Color> getColor() {
    return color;
  }
  
  public Supplier<BossBar.Overlay> getOverlay() {
    return overlay;
  }
  
  public BossBar getBossBar() {
    return bossBar;
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
  
  public void hideAll() {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      onlinePlayer.hideBossBar(bossBar);
    }
  }
  
  public void update() {
    bossBar.name(title.get());
    bossBar.color(color.get());
    bossBar.overlay(overlay.get());
    bossBar.progress(progress.get());
    
    Collection<UUID> newViewers = viewers.get();
    
    previousViewers.removeAll(newViewers);
    previousViewers.stream()
                   .map(Bukkit::getPlayer)
                   .filter(Objects::nonNull)
                   .forEach(player -> {
                     player.hideBossBar(bossBar);
                   });
    
    newViewers.stream()
              .map(Bukkit::getPlayer)
              .filter(Objects::nonNull)
              .forEach(player -> {
                if (shouldDisplay.apply(player)) {
                  player.showBossBar(bossBar);
                } else {
                  player.hideBossBar(bossBar);
                }
              });
    
    previousViewers = newViewers;
  }
}
