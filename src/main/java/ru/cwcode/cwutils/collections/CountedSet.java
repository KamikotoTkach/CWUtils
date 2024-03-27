package ru.cwcode.cwutils.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Структура данных для подсчёта дублей в коллекции. Хранит данные в HashMap (T, Integer)
 */
public class CountedSet<T> {
  HashMap<T, Integer> map;
  
  public CountedSet(Collection<T> items) {
    map = new HashMap<>();
    for (T x : items) {
      map.put(x, map.getOrDefault(x, 0) + 1);
    }
  }
  
  public CountedSet(HashMap<T, Integer> map) {
    this.map = map;
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
  
  public Map.Entry<T, Integer> minEntry() {
    Map.Entry<T, Integer> min = null;
    
    for (Map.Entry<T, Integer> value : map.entrySet()) {
      if (min == null || value.getValue() < min.getValue()) {
        min = value;
      }
    }
    
    return min;
  }
  
  public Map.Entry<T, Integer> maxEntry() {
    Map.Entry<T, Integer> max = null;
    
    for (Map.Entry<T, Integer> value : map.entrySet()) {
      if (max == null || value.getValue() > max.getValue()) {
        max = value;
      }
    }
    
    return max;
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
