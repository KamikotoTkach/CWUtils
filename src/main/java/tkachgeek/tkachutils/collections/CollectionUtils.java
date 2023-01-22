package tkachgeek.tkachutils.collections;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CollectionUtils {
  static Random rand = new Random();
  
  public static <T> @Nullable T getRandomListEntry(List<T> list) {
    if (list == null) return null;
    if (list.size() == 0) return null;
    if (list.size() == 1) return list.get(0);
    return list.get(rand.nextInt(list.size()));
  }
  
  @SafeVarargs
  public static <T> T getRandomArrayEntry(T... colors) {
    if (colors.length == 0) return null;
    if (colors.length == 1) return colors[0];
    return colors[rand.nextInt(colors.length)];
  }
  
  public static void shuffleArray(int[] ar) {
    for (int i = ar.length - 1; i > 0; i--) {
      int index = rand.nextInt(i + 1);
      
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
}
