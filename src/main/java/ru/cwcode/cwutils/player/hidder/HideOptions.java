package ru.cwcode.cwutils.player.hidder;

import java.util.*;
import java.util.function.Predicate;

public final class HideOptions<Option extends HideOption> {
  private final Map<UUID, Option> hiddenPlayers = new HashMap<>();
  
  public HideOptions() {
  }
  
  public boolean hasOption(UUID hidden) {
    return getHideOption(hidden).isPresent();
  }
  
  public Optional<Option> getHideOption(UUID hidden) {
    Option hideOption = hiddenPlayers.get(hidden);
    if (hideOption == null) return Optional.empty();
    
    if (!isValid(hideOption)) {
      hiddenPlayers.remove(hidden);
      return Optional.empty();
    }
    
    return Optional.of(hideOption);
  }
  
  public void hide(UUID hidden, Option options) {
    hiddenPlayers.put(hidden, options);
  }
  
  public void show(UUID hidden, Predicate<Option> showIf) {
    Option hideOption = hiddenPlayers.get(hidden);
    if (hideOption != null && showIf.test(hideOption)) {
      hiddenPlayers.remove(hidden);
    }
  }
  
  public void show(UUID hidden) {
    show(hidden, (option -> true));
  }
  
  public boolean isEmpty() {
    return hiddenPlayers.isEmpty();
  }
  
  public Map<UUID, Option> getHideOptions() {
    if (!hiddenPlayers.isEmpty()) {
      hiddenPlayers.entrySet().removeIf((entry) -> !isValid(entry.getValue()));
    }
    
    return Collections.unmodifiableMap(hiddenPlayers);
  }
  
  private boolean isValid(Option hideOption) {
    return !hideOption.isExpired();
  }
}
