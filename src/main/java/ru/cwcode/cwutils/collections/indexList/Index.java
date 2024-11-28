package ru.cwcode.cwutils.collections.indexList;

import java.util.function.Function;

public abstract class Index<K, E> {
  protected final IndexList<E> list;
  protected final Function<E, K> keyExtractor;
  
  protected Index(IndexList<E> list, Function<E, K> keyExtractor) {
    this.list = list;
    this.keyExtractor = keyExtractor;
    
    list.registerIndex(this);
    
    for (int i = 0; i < list.elements.size(); i++) {
      onElementAdded(list.elements.get(i));
    }
  }
  
  protected abstract void onElementAdded(E e);
  
  protected abstract void onElementRemoved(E e);
  
  public K keyOf(E e) {
    return keyExtractor.apply(e);
  }
}
