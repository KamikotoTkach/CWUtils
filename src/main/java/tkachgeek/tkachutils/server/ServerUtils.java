package tkachgeek.tkachutils.server;

import org.bukkit.Bukkit;
import tkachgeek.tkachutils.numbers.NumbersUtils;

public class ServerUtils {
    public static boolean isVersionBefore1_16_5() {
        String version = Bukkit.getBukkitVersion()
                .substring(0, 6)
                .replaceAll("\\.", "");

        if (NumbersUtils.isInteger(version)) {
            int number = Integer.parseInt(version);
            return number < 1165;
        }

        return true;
    }
}
