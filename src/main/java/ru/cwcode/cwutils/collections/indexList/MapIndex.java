package ru.cwcode.cwutils.collections.indexList;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MapIndex<K, E> extends Index<K, E> {
  protected Map<K, E> map = new HashMap<>(); //temp map
  
  public MapIndex(Map<K, E> map, IndexList<E> list, Function<E, K> keyExtractor) {
    super(list, keyExtractor);
    map.putAll(this.map);
    this.map = map;
  }
  
  public E get(K k) {
    return map.get(k);
  }
  
  @Override
  protected void onElementAdded(E e) {
    map.put(keyOf(e), e);
  }
  
  @Override
  protected void onElementRemoved(E e) {
    map.remove(keyOf(e));
  }
}
