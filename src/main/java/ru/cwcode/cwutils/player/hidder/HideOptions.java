package ru.cwcode.cwutils.player.hidder;

import java.util.*;
import java.util.function.Predicate;

public final class HideOptions<Option extends HideOption> {
  private final Map<UUID, List<Option>> hiddenPlayers = new HashMap<>();
  
  public HideOptions() {
  }
  
  public boolean hasOptions(UUID hidden) {
    return !getHideOptions(hidden).isEmpty();
  }
  
  public void hide(UUID hidden, Option option) {
    hiddenPlayers.computeIfAbsent(hidden, k -> new LinkedList<>()).add(option);
  }
  
  public void show(UUID hidden, Predicate<Option> showIf) {
    List<Option> hideOptions = getHideOptions(hidden);
    hideOptions.removeIf(showIf);
    
    if (hideOptions.isEmpty()) hiddenPlayers.remove(hidden);
  }
  
  public void show(UUID hidden) {
    show(hidden, (option -> true));
  }
  
  public boolean isEmpty(UUID hidden) {
    return getHideOptions(hidden).isEmpty();
  }
  
  public boolean isEmpty() {
    return hiddenPlayers.isEmpty();
  }
  
  public List<Option> getHideOptions(UUID hidden) {
    List<Option> hideOptions = getOptions(hidden);
    hideOptions.removeIf(option -> !option.isValid());
    
    if (hideOptions.isEmpty()) hiddenPlayers.remove(hidden);
    
    return hideOptions;
  }
  
  public Map<UUID, List<Option>> getHideOptions() {
    if (hiddenPlayers.isEmpty()) return Map.of();
    
    Map<UUID, List<Option>> hiddenPlayers = new HashMap<>();
    for (UUID hidden : new HashSet<>(this.hiddenPlayers.keySet())) {
      hiddenPlayers.computeIfAbsent(hidden, k -> new LinkedList<>())
                   .addAll(getHideOptions(hidden));
    }
    
    return Collections.unmodifiableMap(hiddenPlayers);
  }
  
  private List<Option> getOptions(UUID hidden) {
    List<Option> options = this.hiddenPlayers.getOrDefault(hidden, new LinkedList<>());
    if (this.hiddenPlayers.containsKey(hidden) && options.isEmpty()) this.hiddenPlayers.remove(hidden);
    
    return options;
  }
}
