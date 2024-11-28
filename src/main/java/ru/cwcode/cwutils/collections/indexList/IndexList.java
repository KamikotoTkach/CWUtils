package ru.cwcode.cwutils.collections.indexList;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class IndexList<E> implements List<E> {
  List<E> elements;
  List<Index<?, E>> indexes = new ArrayList<>();
  
  public IndexList(List<E> elements) {
    this.elements = elements;
  }
  
  public void registerIndex(@NotNull Index<?, E> index) {
    indexes.add(index);
  }
  
  public boolean add(E element) {
    elements.add(element);
    
    notifyAdd(element);
    
    return true;
  }
  
  public E remove(int index) {
    E removed = elements.remove(index);
    
    notifyRemove(removed);
    
    return removed;
  }
  
  //DELEGATES
  
  @Override
  public int size() {
    return elements.size();
  }
  
  @Override
  public boolean isEmpty() {
    return elements.isEmpty();
  }
  
  @Override
  public boolean contains(Object o) {
    return elements.contains(o);
  }
  
  @NotNull
  @Override
  public Iterator<E> iterator() {
    return elements.iterator();
  }
  
  @NotNull
  @Override
  public Object[] toArray() {
    return elements.toArray();
  }
  
  @NotNull
  @Override
  public <T> T[] toArray(@NotNull T[] a) {
    return elements.toArray(a);
  }
  
  @Override
  public boolean remove(Object o) {
    boolean removed = elements.remove(o);
    
    notifyRemove((E) o);
    
    return removed;
  }
  
  @Override
  public boolean containsAll(@NotNull Collection<?> c) {
    return elements.containsAll(c);
  }
  
  @Override
  public boolean addAll(@NotNull Collection<? extends E> c) {
    boolean added = elements.addAll(c);
    
    for (E e : c) {
      notifyAdd(e);
    }
    
    return added;
  }
  
  @Override
  public boolean addAll(int index, @NotNull Collection<? extends E> c) {
    boolean added = elements.addAll(index, c);
    
    for (E e : c) {
      notifyAdd(e);
    }
    
    return added;
  }
  
  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    boolean removed = elements.removeAll(c);
    
    for (Object e : c) {
      notifyRemove((E) e);
    }
    
    return removed;
  }
  
  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void replaceAll(UnaryOperator<E> operator) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void sort(Comparator<? super E> c) {
    elements.sort(c);
  }
  
  @Override
  public void clear() {
    for (E e : elements) {
      notifyRemove(e);
    }
    
    elements.clear();
  }
  
  @Override
  public boolean equals(Object o) {
    return elements.equals(o);
  }
  
  @Override
  public int hashCode() {
    return elements.hashCode();
  }
  
  @Override
  public E get(int index) {
    return elements.get(index);
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
    
    notifyAdd(element);
  }
  
  @Override
  public int indexOf(Object o) {
    return elements.indexOf(o);
  }
  
  @Override
  public int lastIndexOf(Object o) {
    return elements.lastIndexOf(o);
  }
  
  @NotNull
  @Override
  public ListIterator<E> listIterator() {
    return elements.listIterator();
  }
  
  @NotNull
  @Override
  public ListIterator<E> listIterator(int index) {
    return elements.listIterator(index);
  }
  
  @NotNull
  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public Spliterator<E> spliterator() {
    return elements.spliterator();
  }
  
  @Override
  public void forEach(Consumer<? super E> action) {
    elements.forEach(action);
  }
  
  @Override
  public Stream<E> parallelStream() {
    return elements.parallelStream();
  }
  
  @Override
  public Stream<E> stream() {
    return elements.stream();
  }
  
  @Override
  public boolean removeIf(Predicate<? super E> filter) {
    
    for (E element : elements) {
      if (filter.test(element)) {
        notifyRemove(element);
      }
    }
    
    return elements.removeIf(filter);
  }
  
  @Override
  public <T> T[] toArray(IntFunction<T[]> generator) {
    return elements.toArray(generator);
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
