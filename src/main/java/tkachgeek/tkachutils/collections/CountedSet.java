package tkachgeek.tkachutils.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Структура данных для подсчёта дублей в коллекции. Хранит данные в HashMap (T, Integer)
 */
public class CountedSet<T> {
  HashMap<T, Integer> map = new HashMap<>();
  
  public CountedSet(Collection<T> items) {
    items.forEach(x -> {
      map.put(x, map.getOrDefault(x, 0) + 1);
    });
  }
  
  public Set<T> getItems() {
    return map.keySet();
  }
  
  /**
   * @return 0, если объекта нет в коллекции
   */
  public int getQuantity(T item) {
    return map.getOrDefault(item, 0);
  }
  
  public boolean has(T item) {
    return map.containsKey(item);
  }
  
  public int add(T item, int count) {
    map.put(item, map.getOrDefault(item, 0) + count);
    return map.get(item);
  }
  
  public HashMap<T, Integer> getEntries() {
    return map;
  }
}
