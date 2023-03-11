package tkachgeek.tkachutils.text;

import java.util.*;
import java.util.regex.Pattern;

public class StringUtils {
  public static final Comparator<AbstractMap.SimpleImmutableEntry<String, Integer>> COMPARATOR = Comparator.comparingInt(AbstractMap.SimpleImmutableEntry::getValue);
  private static final Pattern pattern = Pattern.compile("^[a-zA-Zа-яА-Я]+[\\wа-яА-Я]*$");
  
  public static int searchSimilarity(String string, String written) {
    
    if (string.equals(written)) return Integer.MAX_VALUE;
    if (string.startsWith(written)) return Integer.MAX_VALUE;
    if (string.contains(written)) return (int) Math.pow(written.length(), 2);
    
    int score = 0;
    
    int stringPointer = 0;
    int stringLength = string.length();
    
    int writtenPointer = 0;
    int writtenLength = written.length();
    
    var writtenArray = written.toCharArray();
    var stringArray = string.toCharArray();
    
    boolean applyMultiplier = true;
    
    while (writtenPointer < writtenLength && stringPointer < stringLength) {
      if (writtenArray[writtenPointer++] == stringArray[stringPointer]) {
        score += applyMultiplier ? 10 : 1;
        stringPointer++;
        applyMultiplier = true;
      } else {
        score--;
        applyMultiplier = false;
      }
    }
    
    return score;
  }
  
  public static int searchSimilarity(String string, String written, boolean ignoreCase) {
    return ignoreCase ? searchSimilarity(string.toLowerCase(), written.toLowerCase()) : searchSimilarity(string, written);
  }
  
  public static List<String> getSuggestions(List<String> variants, String written) {
    return getSuggestions(variants, written, Integer.MAX_VALUE);
  }
  
  public static List<String> getSuggestions(List<String> variants, String written, int limit) {
    if (written.length() == 0) return variants;
  
    List<AbstractMap.SimpleImmutableEntry<String, Integer>> toSort = new ArrayList<>();
    for (String x : variants) {
      var stringIntegerSimpleImmutableEntry = new AbstractMap.SimpleImmutableEntry<>(x, searchSimilarity(x, written, true));
      if (stringIntegerSimpleImmutableEntry.getValue() > 0) {
        toSort.add(stringIntegerSimpleImmutableEntry);
      }
    }
  
    toSort.sort(COMPARATOR.reversed());
  
    List<String> list = new ArrayList<>();
    long limit1 = limit;
    for (var stringIntegerSimpleImmutableEntry : toSort) {
      if (limit1-- == 0) break;
      String key = stringIntegerSimpleImmutableEntry.getKey();
      list.add(key);
    }
    return list;
  }
  
  public static List<String> splitEqually(String text, int size) {
    List<String> ret = new ArrayList<>((text.length() + size - 1) / size);
    
    for (int start = 0; start < text.length(); start += size) {
      ret.add(text.substring(start, Math.min(text.length(), start + size)));
    }
    return ret;
  }
  
  public static boolean isValidName(String name) {
    return pattern.matcher(name).find();
  }
  
  public static boolean containsIgnoreCase(String source, String toSearch) {
    return Objects.equals(source, toSearch) || source.toLowerCase().contains(toSearch.toLowerCase());
  }
}
