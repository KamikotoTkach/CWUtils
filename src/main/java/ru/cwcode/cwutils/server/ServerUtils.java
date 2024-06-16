package ru.cwcode.cwutils.server;

import ru.cwcode.cwutils.numbers.NumbersUtils;

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
  
  /**
   * @deprecated <p> Используйте {@link PaperServerUtils#isVersionBefore1_16_5()}
   */
  @Deprecated
  public static boolean isVersionBefore1_16_5() {
    return PaperServerUtils.isVersionBefore1_16_5();
  }
  
  /**
   * @deprecated <p> Используйте {@link PaperServerUtils#isVersionBeforeOrEqual1_12_2()}
   */
  @Deprecated
  public static boolean isVersionBeforeOrEqual1_12_2() {
    return PaperServerUtils.isVersionBeforeOrEqual1_12_2();
  }
  
  /**
   * @deprecated <p> Используйте {@link PaperServerUtils#isVersionGreater_1_16_5()}
   */
  @Deprecated
  public static boolean isVersionGreater_1_16_5() {
    return PaperServerUtils.isVersionGreater("1.16.5");
  }
  
  /**
   * @deprecated <p> Используйте {@link PaperServerUtils#isVersionGreater(String)}
   */
  @Deprecated
  public static boolean isVersionGreater(String version) {
    return PaperServerUtils.isVersionGreater(version);
  }
}
