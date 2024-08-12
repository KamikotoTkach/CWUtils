package ru.cwcode.cwutils.l10n;

import ru.cwcode.cwutils.collections.CollectionUtils;
import ru.cwcode.cwutils.logger.Logger;

import java.util.Locale;

public class L10n {
  LocalizationRepository repository;
  String locale;
  Logger logger;
  
  public L10n(L10nPlatform l10nPlatform) {
    logger = l10nPlatform.getLogger();
    
    repository = new PluginLocalizationRepository(l10nPlatform);
    
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
      logger.warn("Invalid locale: " + locale);
      return;
    }
    
    this.locale = locale;
    logger.warn("Selected locale: " + locale);
  }
}
