package tkachgeek.tkachutils.text;

import org.bukkit.Bukkit;

import java.util.HashMap;

public class Declinations {
  static HashMap<String, HashMap<DeclinationsType, String>> wordlist = new HashMap<>();
  
  public static void registerForNumbers(String key, String _1, String _234, String _056789) {
    HashMap<DeclinationsType, String> map = getOrDefault(key);
    
    map.put(DeclinationsType.nominative, _1);
    map.put(DeclinationsType.genitive, _234);
    map.put(DeclinationsType.multiple_genitive, _056789);
  }
  
  private static HashMap<DeclinationsType, String> getOrDefault(String key) {
    if (!wordlist.containsKey(key)) {
      return wordlist.put(key, new HashMap<>());
    } else {
      return wordlist.get(key);
    }
  }
  
  public static void register(String key, DeclinationsType type, String value) {
    getOrDefault(key).put(type, value);
  }
  
  public static String cast(String key, int number) {
    number = Math.abs(number);
    if (number > 100) {
      number %= 100;
    }
    if (number > 20) {
      number %= 20;
    }
    
    if (!wordlist.containsKey(key)) Bukkit.getLogger().warning("Склонения для " + key + " не зарегистрированы");
    
    switch (number) {
      case 1:
        return cast(key, DeclinationsType.nominative);
      case 2:
      case 3:
      case 4:
        return cast(key, DeclinationsType.genitive);
      default:
        return cast(key, DeclinationsType.multiple_genitive);
    }
  }
  
  public static String cast(String key, DeclinationsType type) {
    return getOrDefault(key).getOrDefault(type, key);
  }
}
