package ru.cwcode.cwutils.l10n;

import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import ru.cwcode.cwutils.collections.CollectionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

public class L10n {
  private final L10nPlatform l10nPlatform;
  LocalizationRepository repository;
  String locale;
  
  public L10n(L10nPlatform l10nPlatform) {
    this.l10nPlatform = l10nPlatform;
    
    this.repository = new PluginLocalizationRepository(l10nPlatform);
    
    loadDefaultLocale();
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
  
  @Blocking
  public void setLocale(String locale, boolean persist) {
    if (Locale.forLanguageTag(locale) == null) {
      l10nPlatform.getLogger().warn("Invalid locale: " + locale);
      return;
    }
    
    this.locale = locale;
    l10nPlatform.getLogger().warn("Selected locale: " + locale);
    
    if (persist) storeSelectedLocaleAtFile();
  }
  
  public Set<String> getAvailableLocales() {
    return repository.getAvailableLocales();
  }
  
  private void loadDefaultLocale() {
    Properties selectedLocale = new Properties();
    
    try {
      File selectedLocaleFile = getSelectedLocaleFile();
      
      if (selectedLocaleFile.exists()) {
        selectedLocale.load(new InputStreamReader(new FileInputStream(selectedLocaleFile), StandardCharsets.UTF_8));
        String locale = selectedLocale.getProperty("locale");
        
        if (locale != null) {
          setLocale(locale, false);
          return;
        }
      } else {
        l10nPlatform.getLogger().info("No selected locale file found. Using system locale.");
      }
    } catch (IOException e) {
      l10nPlatform.getLogger().warn("Cannot read selected locale file");
    }
    
    setLocale(Locale.getDefault().getLanguage(), true);
  }
  
  private @NotNull File getSelectedLocaleFile() {
    return l10nPlatform.getDataDirectory()
                       .resolve("locale")
                       .resolve("selectedLocale.properties")
                       .toFile();
  }
  
  private void storeSelectedLocaleAtFile() {
    Properties selectedLocale = new Properties();
    selectedLocale.setProperty("locale", locale);
    
    try (FileWriter fileWriter = new FileWriter(getSelectedLocaleFile())) {
      selectedLocale.store(fileWriter, "");
    } catch (IOException e) {
      l10nPlatform.getLogger().warn("Cannot write selected locale file");
      e.printStackTrace();
    }
  }
}
