package ru.cwcode.cwutils.player.hidder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Predicate;

public class PlayerHider<Option extends HideOption> {
  private final JavaPlugin plugin;
  private final HideOptions<Option> hiddenForAllPlayers = new HideOptions<>();
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
  
  public boolean isHiddenFor(UUID hidden, UUID player) {
    if (hidden.equals(player)) return false;
    
    boolean isHiddenForAll = isHiddenForAll(hidden);
    
    HideOptions<Option> hideOptions = getHiddenPlayers(player);
    List<Option> options = hideOptions.getHideOptions(hidden);
    if (options.isEmpty()) return isHiddenForAll;
    
    Option hideOption = options.get(options.size() - 1);
    if (hideOption.getType() == HideType.HIDE_ONLY_FOR) return true;
    if (hideOption.getType() == HideType.VIEW_ONLY_FOR) return false;
    
    return isHiddenForAll;
  }
  
  public boolean isHiddenFor(Player hiddenPlayer, Player onlinePlayer) {
    return isHiddenFor(hiddenPlayer.getUniqueId(), onlinePlayer.getUniqueId());
  }
  
  public boolean isHiddenForAll(UUID hidden) {
    return !hiddenForAllPlayers.isEmpty(hidden);
  }
  
  public boolean isHiddenForAll(Player hiddenPlayer) {
    return isHiddenForAll(hiddenPlayer.getUniqueId());
  }
  
  public void hide(Player hiddenPlayer, Option hideOption, Player... players) {
    UUID hidden = hiddenPlayer.getUniqueId();
    HideType type = hideOption.getType();
    
    if (type == HideType.VIEW_ONLY_FOR) hiddenForAllPlayers.hide(hidden, hideOption);
    
    for (Player onlinePlayer : players) {
      UUID player = onlinePlayer.getUniqueId();
      if (hidden.equals(player)) continue;
      
      HideOptions<Option> hideOptions = gerOrCreateHiddenPlayers(player);
      hideOptions.hide(hidden, hideOption);
      
      Set<UUID> cash = getOrCreateCache(hidden);
      cash.add(hidden);
      
      updateHidden(hiddenPlayer);
    }
  }
  
  public void hide(Player hiddenPlayer, Option hideOption, Collection<Player> players) {
    hide(hiddenPlayer, hideOption, players.toArray(new Player[0]));
  }
  
  public void hideGroup(Option hideOption, Player... players) {
    for (Player onlinePlayer : players) {
      hide(onlinePlayer, hideOption, players);
    }
  }
  
  public void hideGroup(Option hideOption, Collection<Player> players) {
    hideGroup(hideOption, players.toArray(new Player[0]));
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
    
    if (hiddenForAllPlayers.hasOptions(hidden)) {
      hiddenForAllPlayers.show(hidden, showIf);
      changed = true;
    }
    
    Set<UUID> cache = getCache(hidden);
    if (!cache.isEmpty()) {
      for (UUID player : new HashSet<>(cache)) {
        HideOptions<Option> hideOptions = getHiddenPlayers(player);
        hideOptions.show(hidden, showIf);
        if (hideOptions.isEmpty(hidden)) {
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
