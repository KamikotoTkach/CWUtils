package ru.cwcode.cwutils.l10n;

import ru.cwcode.cwutils.reflection.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PluginLocalizationRepository implements LocalizationRepository {
  private final static String DEFAULT_LOCALE = "en";
  
  Map<String, Map<String, String>> locales = new HashMap<>();
  Map<String, String> defaultLocale;
  
  public PluginLocalizationRepository(L10nPlatform l10nPlatform) {
    
    Set<String> localePropertiesFiles = ReflectionUtils.findMatchingResources(l10nPlatform.getFile(), x -> x.startsWith("locale/")
                                                                                                           && x.endsWith("/locale.properties"));
    
    for (String localePropertiesFile : localePropertiesFiles) {
      loadLocalePropertyFile(l10nPlatform, localePropertiesFile);
    }
    
    if (locales.isEmpty()) {
      l10nPlatform.getLogger().info("No locale properties files were found.");
    } else {
      l10nPlatform.getLogger().info("Loaded " + locales.size() + " locales.");
    }
  }
  
  private void loadLocalePropertyFile(L10nPlatform l10nPlatform, String localePropertiesFile) {
    Properties properties = new Properties();
    
    try {
      InputStream localePropertiesStream = l10nPlatform.getResource(localePropertiesFile);
      if (localePropertiesStream == null) {
        l10nPlatform.getLogger().warn("Could not find locale properties file: " + localePropertiesFile);
        return;
      }
      
      properties.load(new InputStreamReader(localePropertiesStream, StandardCharsets.UTF_8));
      
      for (String languageCode : properties.stringPropertyNames()) {
        loadLocale(l10nPlatform, languageCode, properties.getProperty(languageCode));
      }
    } catch (IOException e) {
      l10nPlatform.getLogger().error("Failed to load locale properties file: " + localePropertiesFile);
      e.printStackTrace();
    }
  }
  
  @Override
  public String getEntry(String locale, String key) {
    Map<String, String> localeMap = locales.getOrDefault(locale, getDefaultLocale());
    
    String value = localeMap.get(key);
    
    if (localeMap == getDefaultLocale()) return value;
    
    if (value == null) { // if locale is not default and locale does not contain required key
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
                             .orElseGet(() -> (Map<String, String>) Collections.EMPTY_MAP);
    }
    
    return defaultLocale;
  }
  
  private void loadLocale(L10nPlatform l10nPlatform, String languageCode, String path) {
    InputStream localeStream = l10nPlatform.getResource(path);
    
    if (localeStream == null) {
      l10nPlatform.getLogger().warn("Cannot find locale `" + languageCode + "` at `" + path + "`");
      return;
    }
    
    Properties locale = new Properties();
    
    try {
      locale.load(new InputStreamReader(localeStream, StandardCharsets.UTF_8));
      
      locales.computeIfAbsent(languageCode, k -> new HashMap<>())
             .putAll(((Map<String, String>) (Map) locale));
      
      l10nPlatform.getLogger().info("Loaded locale `" + languageCode + "` from `" + path + "`");
      
    } catch (IOException e) {
      l10nPlatform.getLogger().warn("Cannot load locale `" + languageCode + "`: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
