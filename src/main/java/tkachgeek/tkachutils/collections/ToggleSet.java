package tkachgeek.tkachutils.collections;

import java.util.HashSet;

public class ToggleSet<T> extends HashSet<T> {
  /**
   * Adds the specified element to this set if it is not already present.
   * Removes the specified element from this set if it is present.
   * return false, if element has not been in collection, true if been
   */
  public boolean toggle(T element) {
    return remove(element) || !add(element);
  }
}
