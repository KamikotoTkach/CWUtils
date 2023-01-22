package tkachgeek.tkachutils.datetime;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

public class StringToDuration {
  public static final HashMap<Character, TemporalUnit> units = new HashMap<>();//нужно добавлять ещё в свитчи
  public static final char tickChar = 't';
  private static final char empty = '╪';
  
  static {
    {
      units.put('y', ChronoUnit.YEARS);
      units.put('M', ChronoUnit.MONTHS);
      units.put('w', ChronoUnit.WEEKS);
      units.put('d', ChronoUnit.DAYS);
      units.put('h', ChronoUnit.HOURS);
      units.put('m', ChronoUnit.MINUTES);
      units.put('s', ChronoUnit.SECONDS);
    }
  }
  
  public static boolean isValid(String st) {
    try {
      for (char c : st.toCharArray()) {
        if ((c < '0' || c > '9') && !hasUnit(c) && c != 't') {
          return false;
        }
      }
      parse(st);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  public static Duration parse(String line) {
    HashMap<Character, String> parts = splitParts(line);
    Duration duration = Duration.ofSeconds(0);
    
    for (Map.Entry<Character, String> part : parts.entrySet()) {
      if (hasUnit(part.getKey())) {
        duration = duration.plus(Duration.of(Long.parseLong(part.getValue()), units.get(part.getKey())));
      } else if (part.getKey() == 't') {
        duration = duration.plus(Duration.ofMillis(Long.parseLong(part.getValue()) * 50));
      }
    }
    return duration;
  }
  
  @NotNull
  private static HashMap<Character, String> splitParts(String line) {
    StringBuilder currentPart = new StringBuilder();
    HashMap<Character, String> parts = new HashMap<>();
    
    if (line.length() == 0) return parts;
    
    for (char c : line.toCharArray()) {
      if (c >= '0' && c <= '9') {
        currentPart.append(c);
      } else if (hasUnit(c) || c == 't') {
        if (currentPart.length() > 0) parts.put(c, currentPart.toString());
        currentPart.setLength(0);
      }
    }
    return parts;
  }
  
  private static boolean hasUnit(char c) {
    return units.containsKey(c);
  }
}
