package ru.cwcode.cwutils.persistent;

import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersistentWrapper {
  private static final Gson gson = new Gson();
  
  PersistentDataContainer container;
  List<PersistentField<?, ?>> fields = new ArrayList<>();
  
  public PersistentWrapper(PersistentDataContainer container) {
    this.container = container;
  }
  
  public PersistentWrapper(PersistentDataHolder holder) {
    this.container = holder.getPersistentDataContainer();
  }
  
  public void clear() {
    fields.forEach(PersistentField::remove);
  }
  
  public Map<NamespacedKey, Object> serialize() {
    return fields.stream().collect(Collectors.toMap(x -> x.key, PersistentField::getOrNull));
  }
  
  @Override
  public String toString() {
    return gson.toJson(fields.stream().collect(Collectors.toMap(x -> x.key.toString(), PersistentField::getOrNull)));
  }
  
  protected <T, Z> PersistentField<T, Z> bind(PersistentDataType<T, Z> type, NamespacedKey key) {
    PersistentField<T, Z> field = new PersistentField<>(this, type, key);
    fields.add(field);
    return field;
  }
  
  protected <T, Z> PersistentField<T, Z> bind(PersistentDataType<T, Z> type, String namespacedKey) {
    return bind(type, NamespacedKey.fromString(namespacedKey));
  }
  
  public static class PersistentField<T, Z> {
    private final PersistentWrapper wrapper;
    private final PersistentDataType<T, Z> type;
    private final NamespacedKey key;
    
    public PersistentField(PersistentWrapper wrapper, PersistentDataType<T, Z> type, NamespacedKey key) {
      this.wrapper = wrapper;
      this.type = type;
      this.key = key;
    }
    
    public Z getOrNull() {
      return wrapper.container.get(key, type);
    }
    
    public Optional<Z> get() {
      return Optional.of(getOrNull());
    }
    
    /**
     * @param value new value
     * @return previous value if set
     */
    public Optional<Z> set(Z value) {
      Optional<Z> previous = get();
      wrapper.container.set(key, type, value);
      return previous;
    }
    
    /**
     * @return previous value if set
     */
    public Optional<Z> remove() {
      Optional<Z> previous = get();
      wrapper.container.remove(key);
      return previous;
    }
  }
}
