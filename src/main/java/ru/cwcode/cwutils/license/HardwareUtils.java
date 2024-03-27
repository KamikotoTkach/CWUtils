package ru.cwcode.cwutils.license;

import org.bukkit.Bukkit;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Iterator;

public class HardwareUtils {
  public static boolean checkHwid(String hwid) {
    return getHwidString().equals(hwid);
  }
  
  public static String getHwidString() {
    return String.join("{*}", new String[]
       {
          System.getenv("COMPUTERNAME"),
          String.valueOf(Runtime.getRuntime().availableProcessors()),
          System.getProperty("os.name"),
          System.getProperty("os.build"),
          System.getProperty("os.arch"),
          System.getProperty("user.name"),
          System.getenv("BASEBOARD_PRODUCT"),
          System.getenv("BASEBOARD_SERIALNUMBER"),
          Bukkit.getServer().getIp(),
          getMacAddresses()
       }
    );
  }
  
  public static String getMacAddresses() {
    try {
      StringBuilder builder = new StringBuilder("[");
      
      Iterator<NetworkInterface> it = NetworkInterface.getNetworkInterfaces().asIterator();
      while (it.hasNext()) {
        String str = formatMacAddress(it.next().getHardwareAddress());
        if (str == null) continue;
        
        builder.append(str)
               .append(", ");
      }
      
      if (builder.length() > 2) builder.delete(builder.length() - 2, builder.length());
      
      return builder.append("]").toString();
    } catch (SocketException e) {
      return null;
    }
  }
  
  private static String formatMacAddress(byte[] macAddressBytes) {
    if (macAddressBytes == null) return null;
    
    StringBuilder macAddressBuilder = new StringBuilder();
    
    for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++) {
      String macAddressHexByte = String.format("%02X",
                                               macAddressBytes[macAddressByteIndex]);
      macAddressBuilder.append(macAddressHexByte);
      
      if (macAddressByteIndex != macAddressBytes.length - 1) {
        macAddressBuilder.append(":");
      }
    }
    return macAddressBuilder.toString();
  }
}