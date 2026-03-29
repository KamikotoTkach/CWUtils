package ru.cwcode.cwutils.collections.indexList;

import java.util.Objects;
import java.util.function.Function;

public abstract class Index<K, E> {
  protected final IndexList<E> list;
  private final Function<? super E, ? extends K> keyExtractor;
  private boolean initialized;
  
  protected Index(IndexList<E> list, Function<? super E, ? extends K> keyExtractor) {
    this.list = Objects.requireNonNull(list, "list");
    this.keyExtractor = Objects.requireNonNull(keyExtractor, "keyExtractor");
  }
  
  protected abstract void clearIndex();
  
  protected abstract void onElementAdded(E element);
  
  protected abstract void onElementRemoved(E element);
  
  public final K keyOf(E element) {
    return keyExtractor.apply(element);
  }
  
  final void rebuild() {
    clearIndex();
    for (E element : list) {
      onElementAdded(element);
    }
  }
  
  protected final void initialize() {
    if (initialized) {
      throw new IllegalStateException("Index already initialized");
    }
    
    rebuild();
    list.registerIndex(this);
    initialized = true;
  }
}
