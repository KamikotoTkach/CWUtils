package ru.cwcode.cwutils.collections.indexList;

import com.google.common.collect.ArrayListMultimap;

import java.util.List;
import java.util.function.Function;

public class MultiIndex<K, E> extends Index<K, E> {
  protected ArrayListMultimap<K, E> map = ArrayListMultimap.create();
  
  public MultiIndex(IndexList<E> list, Function<E, K> keyExtractor) {
    super(list, keyExtractor);
  }
  
  public List<E> get(K k) {
    return map.get(k);
  }
  
  @Override
  protected void onElementAdded(E e) {
    map.put(keyOf(e), e);
  }
  
  @Override
  protected void onElementRemoved(E e) {
    map.remove(keyOf(e), e);
  }
}
