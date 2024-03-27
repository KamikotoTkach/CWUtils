package ru.cwcode.cwutils.datetime;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

public class StringToDuration {
  private static final HashMap<Character, TemporalUnit> units = new HashMap<>();
  private static final String unitsString = "%sy %sM %sw %sd %sh %sm %ss";
  
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
  
  public static String fromDuration(Duration duration) {
    long seconds = duration.getSeconds();
    
    long years = seconds / 31536000;
    seconds = (seconds % 31536000);
    
    long months = seconds / 2419200;
    seconds = (seconds % 2419200);
    
    long weeks = seconds / 604800;
    seconds = (seconds % 604800);
    
    long days = seconds / 86400;
    seconds = (seconds % 86400);
    
    long hours = seconds / 3600;
    seconds = (seconds % 3600);
    
    long minutes = seconds / 60;
    seconds = (seconds % 60);
    
    return String.format(unitsString, years, months, weeks, days, hours, minutes, seconds)
                 .replaceFirst("0y", "")
                 .replaceFirst("0M", "")
                 .replaceFirst("0w", "")
                 .replaceFirst("0d", "")
                 .replaceFirst("0h", "")
                 .replaceFirst("0m", "")
                 .replaceFirst("0s", "");
  }
  
  @NotNull
  private static HashMap<Character, String> splitParts(String line) {
    StringBuilder currentPart = new StringBuilder();
    HashMap<Character, String> parts = new HashMap<>();
    
    if (line.isEmpty()) return parts;
    
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
