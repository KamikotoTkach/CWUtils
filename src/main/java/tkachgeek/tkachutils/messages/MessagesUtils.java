package tkachgeek.tkachutils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import tkachgeek.tkachutils.server.ServerUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MessagesUtils {
    private static String default_locale = "en";
    private static final List<String> locales = Arrays.stream(Locale.getAvailableLocales())
            .map(Locale::toString)
            .collect(Collectors.toList());

    public static void send(CommandSender sender, Component message) {
        if (ServerUtils.isVersionBefore1_16_5()) {
            sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(message));
            return;
        }

        sender.sendMessage(message);
    }

    public static void send(CommandSender sender, String message) {
        send(sender, LegacyComponentSerializer.legacySection().deserialize(message));
    }

    public static List<String> getAvailableLocales() {
        return locales;
    }

    public static boolean isAvailableLocale(String locale) {
        return locales.contains(locale.toLowerCase());
    }

    public static String formatLocale(String locale) {
        if (!isAvailableLocale(locale)) {
            locale = default_locale;
        }

        return locale;
    }

    public static String getDefaultLocale() {
        return default_locale;
    }

    public static boolean setDefaultLocale(String locale) {
        if (isAvailableLocale(locale)) {
            default_locale = locale;
            return true;
        }

        return false;
    }
}
