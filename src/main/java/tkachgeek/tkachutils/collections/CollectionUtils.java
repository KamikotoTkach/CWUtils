package tkachgeek.tkachutils.collections;

import org.jetbrains.annotations.Nullable;
import tkachgeek.tkachutils.numbers.Rand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionUtils {
  public static <T> @Nullable T getRandomListEntry(List<T> list) {
    if (list == null) return null;
    if (list.size() == 0) return null;
    if (list.size() == 1) return list.get(0);
    return list.get(Rand.ofInt(list.size()));
  }
  
  @SafeVarargs
  public static <T> T getRandomArrayEntry(T... values) {
    if (values.length == 0) return null;
    if (values.length == 1) return values[0];
    return values[Rand.ofInt(values.length)];
  }
  
  public static void shuffleArray(int[] ar) {
    for (int i = ar.length - 1; i > 0; i--) {
      int index = Rand.ofInt(i + 1);
      
      int a = ar[index];
      ar[index] = ar[i];
      ar[i] = a;
    }
  }
  
  @SafeVarargs
  public static <T> List<T> combine(List<T>... lists) {
    int size = 0;
    for (List<T> list : lists) {
      size += list.size();
    }
    List<T> combined = new ArrayList<>(size);
    for (List<T> list : lists) {
      combined.addAll(list);
    }
    return combined;
  }
  
  public static String getStringOfArray(Object[] values) {
    return toString(values, "\n - ", "", false);
  }
  
  public static String toString(Object[] values, String prefix, String suffix, boolean removeLastSuffix) {
    if (values.length == 0) return "";
    
    StringBuilder sb = new StringBuilder();
    for (Object value : values) {
      sb.append(prefix).append(value).append(suffix);
    }
    
    if (removeLastSuffix) sb.setLength(sb.length() - suffix.length());
    return sb.toString();
  }
  
  public static <T> String toString(List<T> values) {
    return toString(values, "", ", ", true);
  }
  
  public static <T> String toString(List<T> values, String prefix, String suffix, boolean removeLastSuffix) {
    if (values.size() == 0) return "";
  
    StringBuilder sb = new StringBuilder();
    for (T value : values) {
      sb.append(prefix).append(value).append(suffix);
    }
  
    if (removeLastSuffix) sb.setLength(sb.length() - suffix.length());
    return sb.toString();
  }
  
  public static <T> int add(T item, int toAdd, HashMap<T, Integer> map) {
    int newValue = map.getOrDefault(item, 0) + toAdd;
    map.put(item, newValue);
    return newValue;
  }
  
  public static <T> int increment(T item, HashMap<T, Integer> map) {
    return add(item, 1, map);
  }
  
  public static <T> int decrement(T item, HashMap<T, Integer> map) {
    return add(item, -1, map);
  }
}
