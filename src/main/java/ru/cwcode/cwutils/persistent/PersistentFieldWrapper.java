package ru.cwcode.cwutils.persistent;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

public class PersistentFieldWrapper<Z> {
  private final PersistentDataType<?, Z> type;
  private final NamespacedKey key;
  
  public PersistentFieldWrapper(PersistentDataType<?, Z> type, NamespacedKey key) {
    this.type = type;
    this.key = key;
  }
  
  public PersistentFieldWrapper(PersistentDataType<?, Z> type, String key) {
    this(type, NamespacedKey.fromString(key));
  }
  
  public Z getOrNull(PersistentDataContainer container) {
    return container.get(key, type);
  }
  
  public Optional<Z> get(PersistentDataContainer container) {
    return Optional.ofNullable(getOrNull(container));
  }
  
  /**
   * @param value new value
   * @return previous value if set
   */
  public Optional<Z> set(PersistentDataContainer container, Z value) {
    Optional<Z> previous = get(container);
    container.set(key, type, value);
    return previous;
  }
  
  /**
   * @return previous value if set
   */
  public Optional<Z> remove(PersistentDataContainer container) {
    Optional<Z> previous = get(container);
    container.remove(key);
    return previous;
  }
  
  public Z getOrNull(PersistentDataHolder holder) {
    return this.getOrNull(holder.getPersistentDataContainer());
  }
  
  public Optional<Z> get(PersistentDataHolder holder) {
    return this.get(holder.getPersistentDataContainer());
  }
  
  /**
   * @param value new value
   * @return previous value if set
   */
  public Optional<Z> set(PersistentDataHolder holder, Z value) {
    return this.set(holder.getPersistentDataContainer(), value);
  }
  
  /**
   * @return previous value if set
   */
  public Optional<Z> remove(PersistentDataHolder holder) {
    return this.remove(holder.getPersistentDataContainer());
  }
}
