package ru.cwcode.cwutils.l10n;

import ru.cwcode.cwutils.l10n.source.BundledLocalizationSource;
import ru.cwcode.cwutils.l10n.source.DiskLocalizationSource;
import ru.cwcode.cwutils.l10n.source.LocalizationSource;

import java.util.*;

public class PluginLocalizationRepository implements LocalizationRepository {
  private static final String DEFAULT_LOCALE = "en";
  
  private final Map<String, Map<String, String>> locales = new HashMap<>();
  private Map<String, String> defaultLocale;
  
  public PluginLocalizationRepository(L10nPlatform l10nPlatform) {
    List<LocalizationSource> sources = Arrays.asList(
      new BundledLocalizationSource(l10nPlatform),
      new DiskLocalizationSource(l10nPlatform)
    );
    
    for (LocalizationSource source : sources) {
      source.loadInto(locales);
    }
    
    if (locales.isEmpty()) {
      l10nPlatform.getLogger().info("No locale properties files were found.");
    } else {
      l10nPlatform.getLogger().info("Loaded " + locales.size() + " locales.");
    }
  }
  
  @Override
  public String getEntry(String locale, String key) {
    Map<String, String> localeMap = locales.getOrDefault(locale, getDefaultLocale());
    String value = localeMap.get(key);
    
    if (localeMap == getDefaultLocale()) {
      return value;
    }
    
    if (value == null) {
      return getDefaultLocale().get(key);
    }
    
    return value;
  }
  
  @Override
  public Set<String> getAvailableLocales() {
    return locales.keySet();
  }
  
  private Map<String, String> getDefaultLocale() {
    if (defaultLocale != null) return defaultLocale;
    
    defaultLocale = locales.get(DEFAULT_LOCALE);
    
    if (defaultLocale == null) {
      defaultLocale = locales.values().stream()
                             .findFirst()
                             .orElseGet(Collections::emptyMap);
    }
    
    return defaultLocale;
  }
}
