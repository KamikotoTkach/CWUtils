package ru.cwcode.cwutils.messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

public class Messages<MessageInheritor extends Message> {
  private static final HashSet<String> locales = new HashSet<>(
     Arrays.stream(Locale.getAvailableLocales())
           .map(Locale::toString)
           .collect(Collectors.toList())
  );
  private static String default_locale = "en";
  private final HashMap<String, MessageInheritor> messages = new HashMap<>();
  private final MessageInheritor defaultMessage;
  
  public Messages(String locale, MessageInheritor defaultMessage) {
    this.defaultMessage = defaultMessage;
    locale = Messages.formatLocale(locale);
    
    messages.put(locale, defaultMessage);
  }
  
  public Messages(MessageInheritor defaultMessage) {
    this(Messages.getDefaultLocale(), defaultMessage);
  }
  
  public static HashSet<String> getAvailableLocales() {
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
  
  public Messages add(String locale, MessageInheritor message) {
    locale = Messages.formatLocale(locale);
    messages.put(locale, message);
    
    return this;
  }
  
  public Messages add(MessageInheritor message) {
    return this.add(Messages.getDefaultLocale(), message);
  }
  
  public MessageInheritor get(String locale) {
    return messages.getOrDefault(locale.toLowerCase(), this.defaultMessage);
  }
  
  public MessageInheritor get() {
    return this.get(Messages.getDefaultLocale());
  }
}
