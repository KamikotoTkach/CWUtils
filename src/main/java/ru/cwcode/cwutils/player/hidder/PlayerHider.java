package ru.cwcode.cwutils.player.hidder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class PlayerHider<Option extends HideOption> {
  private final JavaPlugin plugin;
  private final Map<UUID, Option> hiddenForAllPlayers = new HashMap<>();
  private final Map<UUID, HideOptions<Option>> options = new HashMap<>();
  private final Map<UUID, Set<UUID>> cache = new HashMap<>();
  
  public PlayerHider(JavaPlugin plugin, long period) {
    this.plugin = plugin;
    Bukkit.getPluginManager().registerEvents(new HiderListener(this), plugin);
    Bukkit.getScheduler().runTaskTimer(plugin, this::scheduler, 0, period);
  }
  
  public HideOptions<Option> getHiddenPlayers(UUID player) {
    HideOptions<Option> hideOptions = options.getOrDefault(player, new HideOptions<>());
    if (options.containsKey(player) && hideOptions.isEmpty()) options.remove(player);
    
    return hideOptions;
  }
  
  public HideOptions<Option> getHiddenPlayers(Player onlinePlayer) {
    return getHiddenPlayers(onlinePlayer.getUniqueId());
  }
  
  public boolean isHidden(UUID player, UUID hidden) {
    return getHiddenPlayers(player).hasOption(hidden);
  }
  
  public boolean isHidden(Player onlinePlayer, Player hiddenPlayer) {
    return isHidden(onlinePlayer.getUniqueId(), hiddenPlayer.getUniqueId());
  }
  
  public boolean isHiddenFor(UUID hidden, UUID player) {
    if (hidden.equals(player)) return false;
    
    boolean isHiddenForAll = isHiddenForAll(hidden);
    
    HideOptions<Option> hideOptions = getHiddenPlayers(player);
    if (hideOptions.isEmpty()) return isHiddenForAll;
    
    Option hideOption = hideOptions.getHideOption(hidden).orElse(null);
    if (hideOption == null) return isHiddenForAll;
    
    if (hideOption.getType() == HideType.HIDE_ONLY_FOR) return true;
    if (hideOption.getType() == HideType.VIEW_ONLY_FOR) return false;
    
    return isHiddenForAll;
  }
  
  public boolean isHiddenFor(Player hiddenPlayer, Player onlinePlayer) {
    return isHiddenFor(hiddenPlayer.getUniqueId(), onlinePlayer.getUniqueId());
  }
  
  public boolean isHiddenForAll(UUID hidden) {
    HideOption hideOption = hiddenForAllPlayers.get(hidden);
    if (hideOption == null) return false;
    
    if (hideOption.isExpired()) {
      hiddenForAllPlayers.remove(hidden);
      return false;
    }
    
    return true;
  }
  
  public boolean isHiddenForAll(Player hiddenPlayer) {
    return isHiddenForAll(hiddenPlayer.getUniqueId());
  }
  
  public void hide(Player hiddenPlayer, Option hideOption, Player... players) {
    UUID hidden = hiddenPlayer.getUniqueId();
    HideType type = hideOption.getType();
    
    if (type == HideType.VIEW_ONLY_FOR) hiddenForAllPlayers.put(hidden, hideOption);
    
    for (Player player : players) {
      UUID hiddenFor = player.getUniqueId();
      
      HideOptions<Option> hideOptions = gerOrCreateHiddenPlayers(hidden);
      hideOptions.hide(hidden, hideOption);
      
      Set<UUID> cash = getOrCreateCache(hidden);
      cash.add(hiddenFor);
      
      updateHidden(hiddenPlayer);
    }
  }
  
  public void showFor(Player hiddenPlayer, Player onlinePlayer) {
    UUID hidden = hiddenPlayer.getUniqueId();
    UUID player = onlinePlayer.getUniqueId();
    
    onlinePlayer.showPlayer(plugin, hiddenPlayer);
    
    getHiddenPlayers(player).show(hidden);
    getCache(hidden).remove(player);
  }
  
  public void show(Player hiddenPlayer, Predicate<Option> showIf) {
    UUID hidden = hiddenPlayer.getUniqueId();
    boolean changed = false;
    
    Option hideOption = hiddenForAllPlayers.get(hidden);
    if (hideOption != null && showIf.test(hideOption)) {
      hiddenForAllPlayers.remove(hidden);
      changed = true;
    }
    
    Set<UUID> cache = getCache(hidden);
    if (!cache.isEmpty()) {
      for (UUID player : new HashSet<>(cache)) {
        HideOptions<Option> hideOptions = getHiddenPlayers(player);
        hideOptions.show(hidden, showIf);
        if (!hideOptions.hasOption(hidden)) {
          cache.remove(player);
          changed = true;
        }
      }
    }
    
    if (changed) updateHidden(hiddenPlayer);
  }
  
  public void show(Player hiddenPlayer) {
    show(hiddenPlayer, (option -> true));
  }
  
  public void updateHidden(Player hiddenPlayer) {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      boolean isHidden = isHiddenFor(hiddenPlayer, onlinePlayer);
      if (isHidden) {
        onlinePlayer.hidePlayer(plugin, hiddenPlayer);
      } else {
        onlinePlayer.showPlayer(plugin, hiddenPlayer);
      }
    }
  }
  
  public void updateViewer(Player onlinePlayer) {
    for (Player hiddenPlayer : Bukkit.getOnlinePlayers()) {
      boolean isHidden = isHiddenFor(hiddenPlayer, onlinePlayer);
      if (isHidden) {
        onlinePlayer.hidePlayer(plugin, hiddenPlayer);
      } else {
        onlinePlayer.showPlayer(plugin, hiddenPlayer);
      }
    }
  }
  
  private void scheduler() {
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      updateHidden(onlinePlayer);
    }
  }
  
  private HideOptions<Option> gerOrCreateHiddenPlayers(UUID player) {
    return options.computeIfAbsent(player, k -> new HideOptions<>());
  }
  
  private Set<UUID> getCache(UUID hidden) {
    Set<UUID> cache = this.cache.getOrDefault(hidden, new HashSet<>());
    if (this.cache.containsKey(hidden) && cache.isEmpty()) this.cache.remove(hidden);
    
    return cache;
  }
  
  private Set<UUID> getOrCreateCache(UUID hidden) {
    return cache.computeIfAbsent(hidden, k -> new HashSet<>());
  }
}
