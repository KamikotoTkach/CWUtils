package ru.cwcode.cwutils.persistent;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersistentWrapper {
  protected static final Gson gson = new Gson();
  
  protected static final LoadingCache<String, NamespacedKey> STRING_TO_NAMESPACED_KEY_CACHE = CacheBuilder.newBuilder()
                                                                                                          .softValues()
                                                                                                          .maximumSize(1000)
                                                                                                          .build(CacheLoader.from(NamespacedKey::fromString));
  
  protected PersistentDataContainer container;
  protected List<Field<?>> fields = new LinkedList<>();
  
  public PersistentWrapper(PersistentDataContainer container) {
    this.container = container;
  }
  
  public PersistentWrapper(PersistentDataHolder holder) {
    this.container = holder.getPersistentDataContainer();
  }
  
  public void clear() {
    fields.forEach(Field::remove);
  }
  
  public Map<NamespacedKey, Object> serialize() {
    return fields.stream().collect(Collectors.toMap(x -> x.key, Field::getOrNull));
  }
  
  @Override
  public String toString() {
    return gson.toJson(fields.stream().collect(Collectors.toMap(x -> x.key.toString(), Field::getOrNull)));
  }
  
  protected <Z> Field<Z> bind(PersistentDataType<?, Z> type, NamespacedKey key) {
    Field<Z> field = new Field<>(type, key);
    fields.add(field);
    return field;
  }
  
  protected <Z> Field<Z> bind(PersistentDataType<?, Z> type, String namespacedKey) {
    return bind(type, STRING_TO_NAMESPACED_KEY_CACHE.getUnchecked(namespacedKey));
  }
  
  public class Field<Z> {
    private final PersistentDataType<?, Z> type;
    private final NamespacedKey key;
    
    public Field(PersistentDataType<?, Z> type, NamespacedKey key) {
      this.type = type;
      this.key = key;
    }
    
    public Z getOrNull() {
      return container.get(key, type);
    }
    
    public Optional<Z> get() {
      return Optional.ofNullable(getOrNull());
    }
    
    /**
     * @param value new value
     * @return previous value if set
     */
    public Optional<Z> set(Z value) {
      Optional<Z> previous = get();
      container.set(key, type, value);
      return previous;
    }
    
    /**
     * @return previous value if set
     */
    public Optional<Z> remove() {
      Optional<Z> previous = get();
      container.remove(key);
      return previous;
    }
  }
}
