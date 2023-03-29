package tkachgeek.tkachutils.text;

import tkachgeek.tkachutils.numbers.NumbersUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class StringUtils {
  public static final Comparator<StringIntEntry> SUGGESTION_COMPARATOR = Comparator.comparingInt(StringIntEntry::getInt);
  private static final Pattern SAFE_STRING_PATTERN = Pattern.compile("^[\\wа-яА-Я]+[\\wа-яА-Я\\s]*$");
  
  public static int searchSimilarity(String string, String written) {
    
    if (string.equals(written)) return Integer.MAX_VALUE;
    if (string.startsWith(written)) return Integer.MAX_VALUE;
    if (string.contains(written)) return written.length() * written.length();
    
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
    
    List<StringIntEntry> suggestions = new ArrayList<>();
    for (String x : variants) {
      int similarity = searchSimilarity(x, written, true);
      if (similarity > 0) {
        suggestions.add(new StringIntEntry(x, similarity));
      }
    }
    
    suggestions.sort(SUGGESTION_COMPARATOR.reversed());
    
    List<String> limited = new ArrayList<>(NumbersUtils.notGreater(limit, suggestions.size()));
    
    for (StringIntEntry suggestion : suggestions) {
      if (limit-- == 0) break;
      limited.add(suggestion.getString());
    }
    return limited;
  }
  
  public static List<String> splitEqually(String text, int size) {
    List<String> ret = new ArrayList<>((text.length() + size - 1) / size);
    
    for (int start = 0; start < text.length(); start += size) {
      ret.add(text.substring(start, Math.min(text.length(), start + size)));
    }
    return ret;
  }
  
  public static boolean isSafetyString(String string) {
    return SAFE_STRING_PATTERN.matcher(string).find();
  }
  
  public static boolean containsIgnoreCase(String source, String toSearch) {
    return Objects.equals(source, toSearch) || source.toLowerCase().contains(toSearch.toLowerCase());
  }
}
