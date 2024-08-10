package ru.cwcode.cwutils.l10n;

import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.cwutils.collections.CollectionUtils;

import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;

public class L10n {
  LocalizationRepository repository;
  String locale;
  Logger logger;
  
  public L10n(File jarFile, JavaPlugin plugin) {
    logger = plugin.getLogger();
    
    repository = new JavaPluginLocalizationRepository(jarFile, plugin);
    
    setLocale(Locale.getDefault().getLanguage());
  }
  
  public String get(String key, Object... args) {
    String entry = repository.getEntry(getLocale(), key);
    
    if (entry == null) {
      return key + "[" + CollectionUtils.toString(args, "", ", ", true) + "]";
    }
    
    return String.format(entry, args);
  }
  
  public String getLocale() {
    return locale;
  }
  
  public void setLocale(String locale) {
    if (Locale.forLanguageTag(locale) == null) {
      logger.warning("Invalid locale: " + locale);
      return;
    }
    
    this.locale = locale;
    logger.warning("Selected locale: " + locale);
  }
}
