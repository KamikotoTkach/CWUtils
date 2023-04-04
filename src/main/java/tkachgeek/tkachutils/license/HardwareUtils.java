package tkachgeek.tkachutils.license;

import org.bukkit.Bukkit;

public class HardwareUtils {
  public static String getHwidString() {
    return Bukkit.getServer().getIp()
       + "{*}" + System.getProperty("os.name")
       + "{*}" + System.getProperty("user.name")
       + "{*}" + Runtime.getRuntime().availableProcessors()
       + "{*}" + System.getProperty("os.arch")
       + "{*}" + System.getenv("COMPUTERNAME");
  }
  
  public static boolean checkHwid(String hwid) {
    return getHwidString().equals(hwid);
  }
}
