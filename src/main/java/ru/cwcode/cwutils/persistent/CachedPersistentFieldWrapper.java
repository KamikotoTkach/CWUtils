package ru.cwcode.cwutils.persistent;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Duration;
import java.util.Optional;

public class CachedPersistentFieldWrapper<Z> extends PersistentFieldWrapper<Z> {
  private final Cache<PersistentDataContainer, Z> cache = Caffeine.newBuilder()
                                                                  .weakKeys()
                                                                  .expireAfterAccess(Duration.ofMinutes(10))
                                                                  .maximumSize(10_000)
                                                                  .build();
  
  public CachedPersistentFieldWrapper(PersistentDataType<?, Z> type, NamespacedKey key) {
    super(type, key);
  }
  
  public CachedPersistentFieldWrapper(PersistentDataType<?, Z> type, String key) {
    super(type, key);
  }
  
  @Override
  public Z getOrNull(PersistentDataContainer holder) {
    return cache.get(holder, super::getOrNull);
  }
  
  @Override
  public Optional<Z> remove(PersistentDataContainer container) {
    try {
      return super.remove(container);
    } finally {
      cache.invalidate(container);
    }
  }
  
  @Override
  public Optional<Z> set(PersistentDataContainer container, Z value) {
    Optional<Z> previous = super.set(container, value);
    cache.put(container, value);
    return previous;
  }
}
