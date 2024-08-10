package ru.cwcode.cwutils.l10n;

import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.cwutils.reflection.ReflectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JavaPluginLocalizationRepository implements LocalizationRepository {
  private final static String DEFAULT_LOCALE = "en";
  
  Map<String, Map<String, String>> locales = new HashMap<>();
  Map<String, String> defaultLocale;
  
  public JavaPluginLocalizationRepository(File jarFile, JavaPlugin plugin) {
    
    Set<String> localePropertiesFiles = ReflectionUtils.findMatchingResources(jarFile, x -> x.startsWith("locale/")
                                                                                            && x.endsWith("/locale.properties"));
    
    for (String localePropertiesFile : localePropertiesFiles) {
      Properties properties = new Properties();
      
      try {
        InputStream localePropertiesStream = plugin.getResource(localePropertiesFile);
        if (localePropertiesStream == null) {
          plugin.getLogger().warning("Could not find locale properties file: " + localePropertiesFile);
          continue;
        }
        
        properties.load(new InputStreamReader(localePropertiesStream, StandardCharsets.UTF_8));
        
        for (String languageCode : properties.stringPropertyNames()) {
          loadLocale(plugin, languageCode, properties.getProperty(languageCode));
        }
      } catch (IOException e) {
        plugin.getLogger().severe("Failed to load locale properties file: " + localePropertiesFile);
        e.printStackTrace();
      }
    }
    
    if (locales.isEmpty()) {
      plugin.getLogger().info("No locale properties files were found.");
    } else {
      plugin.getLogger().info("Loaded " + locales.size() + " locales.");
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
  
  private void loadLocale(JavaPlugin plugin, String languageCode, String path) {
    InputStream localeStream = plugin.getResource(path);
    
    if (localeStream == null) {
      plugin.getLogger().warning("Cannot find locale `" + languageCode + "` at `" + path + "`");
      return;
    }
    
    Properties locale = new Properties();
    
    try {
      locale.load(new InputStreamReader(localeStream, StandardCharsets.UTF_8));
    } catch (IOException e) {
      plugin.getLogger().warning("Cannot load locale `" + languageCode + "`: " + e.getMessage());
      e.printStackTrace();
    }
    
    locales.computeIfAbsent(languageCode, k -> new HashMap<>())
           .putAll(((Map<String, String>) (Map) locale));
    
    plugin.getLogger().info("Loaded locale `" + languageCode + "` from `" + path + "`");
  }
}
