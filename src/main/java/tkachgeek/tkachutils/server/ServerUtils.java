package tkachgeek.tkachutils.server;

import org.bukkit.Bukkit;
import tkachgeek.tkachutils.numbers.NumbersUtils;

import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerUtils {
  private static final Pattern pattern = Pattern.compile("([\\d.]+)");
  private static final WeakHashMap<String, Integer> weights = new WeakHashMap<>();
  
  public static int getVersionWeight(String version) {
    if (weights.containsKey(version)) return weights.get(version);
    
    Matcher matcher = pattern.matcher(version);
    if (!matcher.find()) return 0;
    
    String[] data = matcher.group(1).split("\\.");
    
    int multiplier = 10000;
    int intVersion = 0;
    for (String num : data) {
      if (!NumbersUtils.isInteger(num)) return 0;
      intVersion += Integer.parseInt(num) * Math.max(multiplier, 1);
      multiplier /= 100;
    }
    
    weights.put(version, intVersion);
    
    return intVersion;
  }
  
  public static boolean isVersionBefore1_16_5() {
    int version = getVersionWeight();
    
    return version < getVersionWeight("1.16.5");
  }
  
  public static boolean isVersionBeforeOrEqual1_12_2() {
    int version = getVersionWeight();
    
    return version <= getVersionWeight("1.12.2");
  }

  public static boolean isVersionGreater(String version) {
    return getVersionWeight() > getVersionWeight(version);
  }

  public static boolean isVersionGreater_1_16_5() {
    return isVersionGreater("1.16.5");
  }
  
  private static int getVersionWeight() {
    return getVersionWeight(Bukkit.getBukkitVersion());
  }
}
