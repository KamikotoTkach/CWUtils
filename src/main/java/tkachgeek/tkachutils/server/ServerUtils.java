package tkachgeek.tkachutils.server;

import org.bukkit.Bukkit;
import tkachgeek.tkachutils.numbers.NumbersUtils;

public class ServerUtils {
    public static boolean isVersionBefore1_16_5() {
        String[] version = Bukkit.getBukkitVersion().substring(0, 6).split("\\.", 3);

        StringBuilder raw = new StringBuilder();
        if (version.length == 3) {
            for (String number : version) {
                if (NumbersUtils.isInteger(number)) {
                    raw.append(number);
                } else {
                    return true;
                }
            }

            int number = Integer.parseInt(raw.toString());
            return number < 1165;
        }

        return true;
    }
}
