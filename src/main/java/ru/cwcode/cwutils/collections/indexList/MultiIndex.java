package ru.cwcode.cwutils.collections.indexList;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MultiIndex<K, E> extends Index<K, E> {
  private final ListMultimap<K, E> map;
  
  public MultiIndex(IndexList<E> list, Function<? super E, ? extends K> keyExtractor) {
    super(list, keyExtractor);
    this.map = ArrayListMultimap.create();
    initialize();
  }
  
  public List<E> get(K key) {
    return Collections.unmodifiableList(new ArrayList<>(map.get(key)));
  }
  
  public boolean containsKey(K key) {
    return map.containsKey(key);
  }
  
  public int count(K key) {
    return map.get(key).size();
  }
  
  @Override
  protected void clearIndex() {
    map.clear();
  }
  
  @Override
  protected void onElementAdded(E element) {
    map.put(keyOf(element), element);
  }
  
  @Override
  protected void onElementRemoved(E element) {
    map.remove(keyOf(element), element);
  }
}