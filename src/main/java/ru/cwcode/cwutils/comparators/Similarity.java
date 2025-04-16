package ru.cwcode.cwutils.comparators;

import java.util.function.BiPredicate;
import java.util.function.Function;

public record Similarity<T, E>(Function<T, E> extractor, BiPredicate<E, E> predicate) {
  
  public boolean isSimilar(T one, T two) {
    if (one == two) return true;
    if (one == null || two == null) return false;
    
    E oneE = extractor.apply(one);
    E twoE = extractor.apply(two);
    
    if (oneE == twoE) return true;
    if (oneE == null || twoE == null) return false;
    
    return predicate.test(oneE, twoE);
  }
}
