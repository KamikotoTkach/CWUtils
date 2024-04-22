package ru.cwcode.cwutils.server;

import org.bukkit.Bukkit;

public class PaperServerUtils {
  public static boolean isVersionBefore1_16_5() {
    int version = getVersionWeight();
    
    return version < ServerUtils.getVersionWeight("1.16.5");
  }
  
  public static boolean isVersionBeforeOrEqual1_12_2() {
    int version = getVersionWeight();
    
    return version <= ServerUtils.getVersionWeight("1.12.2");
  }
  
  public static boolean isVersionGreater_1_16_5() {
    return isVersionGreater("1.16.5");
  }
  
  public static boolean isVersionGreater(String version) {
    return getVersionWeight() > ServerUtils.getVersionWeight(version);
  }
  
  private static int getVersionWeight() {
    return ServerUtils.getVersionWeight(Bukkit.getBukkitVersion());
  }
}
