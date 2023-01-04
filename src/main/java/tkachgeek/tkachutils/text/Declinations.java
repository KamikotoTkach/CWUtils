package tkachgeek.tkachutils.text;

import org.bukkit.Bukkit;

import java.util.HashMap;

public class Declinations {
    static HashMap<String, HashMap<DeclinationsType, String>> wordlist = new HashMap<>();

    public static void registerForNumbers(String key, String _1, String _234, String _056789) {
        wordlist.put(key, new HashMap<>());
        wordlist.get(key).put(DeclinationsType.nominative, _1);
        wordlist.get(key).put(DeclinationsType.genitive, _234);
        wordlist.get(key).put(DeclinationsType.multiple_genitive, _056789);
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
                return wordlist.getOrDefault(key, new HashMap<>()).getOrDefault(DeclinationsType.nominative, key);
            case 2:
            case 3:
            case 4:
                return wordlist.getOrDefault(key, new HashMap<>()).getOrDefault(DeclinationsType.genitive, key);
            default:
                return wordlist.getOrDefault(key, new HashMap<>()).getOrDefault(DeclinationsType.multiple_genitive, key);
        }
    }
}
