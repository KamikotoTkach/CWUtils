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
    for (T x : items) {
      map.put(x, map.getOrDefault(x, 0) + 1);
    }
  }
  
  public Set<T> items() {
    return map.keySet();
  }
  
  public Collection<Integer> values() {
    return map.values();
  }
  
  /**
   * @return 0, если объекта нет в коллекции
   */
  public int quantity(T item) {
    return map.getOrDefault(item, 0);
  }
  
  public boolean has(T item) {
    return map.containsKey(item);
  }
  
  public int add(T item, int count) {
    map.put(item, map.getOrDefault(item, 0) + count);
    return map.get(item);
  }
  
  public int max() {
    int best = 0;
    for (int value : map.values()) {
      if (value > best) {
        best = value;
      }
    }
    return best;
  }
  
  public int min() {
    int min = 0;
    for (int value : map.values()) {
      if (value < min) {
        min = value;
      }
    }
    return min;
  }
  
  public int sum() {
    int sum = 0;
    for (int value : map.values()) {
      sum += value;
    }
    return sum;
  }
  
  public double avg() {
    long sum = 0;
    long count = 0;
    for (int x : map.values()) {
      sum += x;
      count++;
    }
    return count > 0 ? (double) sum / count : 0;
  }
  
  public HashMap<T, Integer> entries() {
    return map;
  }
}
