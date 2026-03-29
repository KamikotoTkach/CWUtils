package ru.cwcode.cwutils.collections.indexList;

import java.util.*;
import java.util.function.UnaryOperator;

public class IndexList<E> extends AbstractList<E> implements RandomAccess {
  private final List<E> elements;
  private final List<Index<?, E>> indexes = new ArrayList<>();
  
  public IndexList() {
    this.elements = new ArrayList<>();
  }
  
  public IndexList(Collection<? extends E> elements) {
    this.elements = new ArrayList<>(Objects.requireNonNull(elements, "elements"));
  }
  
  public void rebuildIndexes() {
    for (Index<?, E> index : indexes) {
      index.rebuild();
    }
  }
  
  @Override
  public E get(int index) {
    return elements.get(index);
  }
  
  @Override
  public int size() {
    return elements.size();
  }
  
  @Override
  public E set(int index, E element) {
    E previous = elements.set(index, element);
    notifyRemove(previous);
    notifyAdd(element);
    return previous;
  }
  
  @Override
  public void add(int index, E element) {
    elements.add(index, element);
    modCount++;
    notifyAdd(element);
  }
  
  @Override
  public E remove(int index) {
    E removed = elements.remove(index);
    modCount++;
    notifyRemove(removed);
    return removed;
  }
  
  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    Objects.requireNonNull(c, "collection");
    
    if (c.isEmpty()) {
      return false;
    }
    
    List<E> added = new ArrayList<>(c);
    elements.addAll(index, added);
    modCount++;
    
    for (E e : added) {
      notifyAdd(e);
    }
    
    return true;
  }
  
  @Override
  public void clear() {
    if (elements.isEmpty()) {
      return;
    }
    
    List<E> removed = new ArrayList<>(elements);
    elements.clear();
    modCount++;
    
    for (E e : removed) {
      notifyRemove(e);
    }
  }
  
  @Override
  public void replaceAll(UnaryOperator<E> operator) {
    Objects.requireNonNull(operator, "operator");
    
    if (elements.isEmpty()) {
      return;
    }
    
    List<E> oldValues = new ArrayList<>(elements.size());
    List<E> newValues = new ArrayList<>(elements.size());
    
    for (int i = 0; i < elements.size(); i++) {
      E oldValue = elements.get(i);
      E newValue = operator.apply(oldValue);
      elements.set(i, newValue);
      
      oldValues.add(oldValue);
      newValues.add(newValue);
    }
    
    modCount++;
    
    for (E oldValue : oldValues) {
      notifyRemove(oldValue);
    }
    for (E newValue : newValues) {
      notifyAdd(newValue);
    }
  }
  
  @Override
  public void sort(Comparator<? super E> c) {
    if (elements.size() < 2) {
      return;
    }
    
    elements.sort(c);
    modCount++;
  }
  
  final void registerIndex(Index<?, E> index) {
    Objects.requireNonNull(index, "index");
    for (Index<?, E> existing : indexes) {
      if (existing == index) {
        throw new IllegalArgumentException("Index already registered");
      }
    }
    indexes.add(index);
  }
  
  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    if (fromIndex == toIndex) {
      return;
    }
    
    List<E> removed = new ArrayList<>(elements.subList(fromIndex, toIndex));
    elements.subList(fromIndex, toIndex).clear();
    modCount++;
    
    for (E e : removed) {
      notifyRemove(e);
    }
  }
  
  protected void notifyRemove(E removed) {
    for (Index<?, E> index : indexes) {
      index.onElementRemoved(removed);
    }
  }
  
  protected void notifyAdd(E element) {
    for (Index<?, E> index : indexes) {
      index.onElementAdded(element);
    }
  }
}
