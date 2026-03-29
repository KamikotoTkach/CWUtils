package ru.cwcode.cwutils.collections.indexList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class MapIndex<K, E> extends Index<K, E> {
  private final Map<K, E> map;
  
  public MapIndex(IndexList<E> list, Function<? super E, ? extends K> keyExtractor) {
    this(new HashMap<>(), list, keyExtractor);
  }
  
  public MapIndex(Map<K, E> map, IndexList<E> list, Function<? super E, ? extends K> keyExtractor) {
    super(list, keyExtractor);
    this.map = Objects.requireNonNull(map, "map");
    this.map.clear();
    initialize();
  }
  
  public E get(K key) {
    return map.get(key);
  }
  
  public boolean containsKey(K key) {
    return map.containsKey(key);
  }
  
  public Map<K, E> asMap() {
    return Map.copyOf(map);
  }
  
  @Override
  protected void clearIndex() {
    map.clear();
  }
  
  @Override
  protected void onElementAdded(E element) {
    K key = keyOf(element);
    E existing = map.putIfAbsent(key, element);
    
    if (existing != null) {
      throw new IllegalStateException(
        "Duplicate key in MapIndex: " + key +
        ", existing element=" + existing +
        ", new element=" + element
      );
    }
  }
  
  @Override
  protected void onElementRemoved(E element) {
    map.remove(keyOf(element), element);
  }
}
