package ru.cwcode.cwutils.l10n.source;

import ru.cwcode.cwutils.l10n.L10nPlatform;
import ru.cwcode.cwutils.reflection.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class BundledLocalizationSource implements LocalizationSource {
  private final L10nPlatform platform;
  
  public BundledLocalizationSource(L10nPlatform platform) {
    this.platform = platform;
  }
  
  @Override
  public void loadInto(Map<String, Map<String, String>> locales) {
    Set<String> localePropertiesFiles = ReflectionUtils.findMatchingResources(
      platform.getFile(),
      path -> path.startsWith("locale/") && path.endsWith("/locale.properties")
    );
    
    for (String localePropertiesFile : localePropertiesFiles) {
      loadIndex(localePropertiesFile, locales);
    }
  }
  
  private void loadIndex(String localePropertiesFile, Map<String, Map<String, String>> locales) {
    try (InputStream in = platform.getResource(localePropertiesFile)) {
      if (in == null) {
        platform.getLogger().warn("Could not find locale properties file: " + localePropertiesFile);
        return;
      }
      
      Properties index = LocalizationIO.loadProperties(in);
      for (String languageCode : index.stringPropertyNames()) {
        loadLocale(languageCode, index.getProperty(languageCode), locales);
      }
    } catch (IOException e) {
      platform.getLogger().error("Failed to load locale properties file: " + localePropertiesFile);
      e.printStackTrace();
    }
  }
  
  private void loadLocale(String languageCode, String path, Map<String, Map<String, String>> locales) {
    try (InputStream in = platform.getResource(path)) {
      if (in == null) {
        platform.getLogger().warn("Cannot find locale `" + languageCode + "` at `" + path + "`");
        return;
      }
      
      Properties locale = LocalizationIO.loadProperties(in);
      LocalizationIO.mergeLocale(locales, languageCode, locale);
      platform.getLogger().info("Loaded bundled locale `" + languageCode + "` from `" + path + "`");
    } catch (IOException e) {
      platform.getLogger().warn("Cannot load locale `" + languageCode + "`: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
