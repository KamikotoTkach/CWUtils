package tkachgeek.tkachutils.server;

import org.bukkit.Bukkit;
import tkachgeek.tkachutils.numbers.NumbersUtils;

public class Server {
    public static boolean isOldVersion() {
        String[] version = Bukkit.getBukkitVersion().substring(0, 6).split("\\.");
        if (version.length == 3) {
            if (NumbersUtils.isInteger(version[0])
                    && Integer.parseInt(version[0]) <= 1) {
                if (NumbersUtils.isInteger(version[1])) {
                    int subVersion = Integer.parseInt(version[1]);
                    if (subVersion < 16
                            || subVersion == 16
                            && NumbersUtils.isInteger(version[2])
                            && Integer.parseInt(version[2]) < 5) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
