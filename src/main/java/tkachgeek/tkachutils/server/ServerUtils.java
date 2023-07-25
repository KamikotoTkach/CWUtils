package tkachgeek.tkachutils.server;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import tkachgeek.tkachutils.numbers.NumbersUtils;

public class ServerUtils {
   @NotNull
   public static int getVersionWeight(String version) {
      String[] data = version.split("-", 2);
      if (data.length != 2) return 0;

      data = data[0].split("\\.");

      int multiplier = 10000;
      int intVersion = 0;
      for (String num : data) {
         if (!NumbersUtils.isInteger(num)) return 0;
         intVersion += Integer.parseInt(num) * Math.max(multiplier, 1);
         multiplier /= 100;
      }

      return intVersion;
   }

   public static boolean isVersionBefore1_16_5() {
      int version = getVersionWeight(Bukkit.getBukkitVersion());

      return version < 1165;
   }

   public static boolean isVersionBeforeOrEqual1_12_2() {
      int version = getVersionWeight(Bukkit.getBukkitVersion());

      return version <= 1122;
   }
}
