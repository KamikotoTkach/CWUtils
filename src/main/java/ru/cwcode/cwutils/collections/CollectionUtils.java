package ru.cwcode.cwutils.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cwcode.cwutils.numbers.Rand;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CollectionUtils {
  
  public static <K, V, tK, tV, M extends Map<tK, tV>> M transformMap(Map<K, V> map,
                                                                     Function<K, tK> keyTransformer,
                                                                     Function<V, tV> valueTransformer,
                                                                     Supplier<M> mapCreator) {
    M result = mapCreator.get();
    
    for (Map.Entry<K, V> x : map.entrySet()) {
      result.put(keyTransformer.apply(x.getKey()), valueTransformer.apply(x.getValue()));
    }
    
    return result;
  }
  
  public static <K, V, tK, tV> HashMap<tK, tV> transformMap(Map<K, V> map,
                                                            Function<K, tK> keyTransformer,
                                                            Function<V, tV> valueTransformer) {
    
    return transformMap(map, keyTransformer, valueTransformer, HashMap::new);
  }
  
  public static <K, V, tV> HashMap<K, tV> transformValue(Map<K, V> map,
                                                         Function<V, tV> valueTransformer) {
    
    return transformMap(map, k -> k, valueTransformer, HashMap::new);
  }
  
  public static <K, V, tK> HashMap<tK, V> transformKey(Map<K, V> map,
                                                       Function<K, tK> keyTransformer) {
    
    return transformMap(map, keyTransformer, v -> v, HashMap::new);
  }
  
  public static <T> @Nullable T getRandomListEntry(List<T> list) {
    if (list == null) return null;
    if (list.isEmpty()) return null;
    if (list.size() == 1) return list.get(0);
    return list.get(Rand.ofInt(list.size()));
  }
  
  @SafeVarargs
  public static <T> T getRandomArrayEntry(T... values) {
    if (values.length == 0) return null;
    if (values.length == 1) return values[0];
    return values[Rand.ofInt(values.length)];
  }
  
  @Deprecated
  public static void shuffleArray(int[] ar) {
    for (int i = ar.length - 1; i > 0; i--) {
      int index = Rand.ofInt(i + 1);
      
      int a = ar[index];
      ar[index] = ar[i];
      ar[i] = a;
    }
  }
  
  public static <T> T[] shuffleArray(T[] array) {
    for (int i = array.length - 1; i > 0; --i) {
      int index = Rand.ofInt(i + 1);
      T a = array[index];
      array[index] = array[i];
      array[i] = a;
    }

    return array;
  }
  
  @SafeVarargs
  public static <T> Collection<T> combine(Collection<T>... lists) {
    int size = 0;
    for (Collection<T> list : lists) {
      size += list.size();
    }
    List<T> combined = new ArrayList<>(size);
    for (Collection<T> list : lists) {
      combined.addAll(list);
    }
    return combined;
  }
  
  /**
   * Используйте HashSet для параметров, если коллекции достаточно большие
   */
  public static <T> boolean containsAny(Collection<T> one, Collection<T> two) {
    for (T t : one) {
      if (two.contains(t)) return true;
    }
    return false;
  }
  
  /**
   * Используйте HashSet для параметров, если коллекции достаточно большие
   */
  public static <T> boolean hasAllElements(Collection<T> toCheck, Collection<T> elements) {
    for (T t : elements) {
      if (!toCheck.contains(t)) return false;
    }
    return true;
  }
  
  public static String toString(Object[] values) {
    return getStringOfArray(values);
  }
  
  @Deprecated(forRemoval = true)
  public static String getStringOfArray(Object[] values) {
    return toString(values, "\n - ", "", false);
  }
  
  public static String toString(Object[] values, String prefix, String suffix, boolean removeLastSuffix) {
    return toString(Arrays.asList(values), prefix, suffix, removeLastSuffix);
  }
  
  public static <T> String toString(List<T> values) {
    return toString((Iterable<?>) values, "", ", ", true);
  }
  
  public static <T> String toString(Iterable<T> values) {
    return toString(values, "", ", ", true);
  }
  
  public static <T> String toString(Collection<T> values) {
    return toString((Iterable<?>) values, "", ", ", true);
  }
  
  public static <T> String toString(List<T> values, String prefix, String suffix, boolean removeLastSuffix) {
    return toString((Iterable<?>) values, prefix, suffix, removeLastSuffix);
  }
  
  public static <T> String toString(Collection<T> values, String prefix, String suffix, boolean removeLastSuffix) {
    return toString((Iterable<?>) values, prefix, suffix, removeLastSuffix);
  }
  
  public static <T> String toString(Iterable<T> values, String prefix, String suffix, boolean removeLastSuffix) {
    
    StringBuilder sb = new StringBuilder();
    for (T value : values) {
      sb.append(prefix).append(value).append(suffix);
    }
    
    if (removeLastSuffix && sb.length() >= suffix.length()) sb.setLength(sb.length() - suffix.length());
    return sb.toString();
  }
  
  public static <T> int increment(T item, Map<T, Integer> map) {
    return add(item, 1, map);
  }
  
  public static <T> int add(T item, int toAdd, Map<T, Integer> map) {
    int newValue = map.getOrDefault(item, 0) + toAdd;
    map.put(item, newValue);
    return newValue;
  }
  
  public static <T> int decrement(T item, Map<T, Integer> map) {
    return add(item, -1, map);
  }
  
  public static <T> T getRandomWeightedElement(Map<T, ? extends Number> map) {
    if (map.isEmpty()) return null;
    
    double totalWeight = map.values().stream().mapToDouble(Number::doubleValue).sum();
    double randomWeight = Rand.ofDouble(totalWeight);
    
    for (Map.Entry<T, ? extends Number> entry : map.entrySet()) {
      randomWeight -= entry.getValue().doubleValue();
      if (randomWeight < 0) {
        return entry.getKey();
      }
    }
    
    return null;
  }
  
  public static <T> T getRandomWeightedElement(Collection<T> elements, Function<T, ? extends Number> chanceExtractor) {
    if (elements == null || elements.isEmpty()) return null;
    
    return getRandomWeightedElement(elements.stream().collect(Collectors.toMap(x -> x, chanceExtractor)));
  }
  
  public static int getFirstAvailableID(Collection<Integer> ids, int minID) {
    HashSet<Integer> idSet = new HashSet<>(ids);
    
    int availableId = minID;
    
    while (idSet.contains(availableId)) {
      availableId++;
    }
    
    return availableId;
  }
  
  public static @NotNull List<String> toStringList(Collection<?> collection) {
    List<String> list = new ArrayList<>();
    
    for (Object entry : collection) {
      list.add(entry.toString());
    }
    
    return list;
  }
}
