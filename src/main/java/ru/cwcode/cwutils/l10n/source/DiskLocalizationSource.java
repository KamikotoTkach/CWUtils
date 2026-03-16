package ru.cwcode.cwutils.l10n.source;

import ru.cwcode.cwutils.l10n.L10nPlatform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

public class DiskLocalizationSource implements LocalizationSource {
  private final L10nPlatform platform;
  
  public DiskLocalizationSource(L10nPlatform platform) {
    this.platform = platform;
  }
  
  @Override
  public void loadInto(Map<String, Map<String, String>> locales) {
    Path root = platform.getDataDirectory().resolve("locale");
    
    if (!Files.exists(root)) {
      return;
    }
    
    try (Stream<Path> walk = Files.walk(root)) {
      walk.filter(Files::isRegularFile)
          .filter(path -> path.getFileName().toString().equals("locale.properties"))
          .forEach(path -> loadIndex(path, locales));
    } catch (IOException e) {
      platform.getLogger().warn("Cannot scan external locale directory: " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  private void loadIndex(Path localePropertiesFile, Map<String, Map<String, String>> locales) {
    try {
      Properties index = LocalizationIO.loadProperties(localePropertiesFile);
      Path baseDir = localePropertiesFile.getParent();
      
      for (String languageCode : index.stringPropertyNames()) {
        Path localeFile = baseDir.resolve(index.getProperty(languageCode)).normalize();
        loadLocale(languageCode, localeFile, locales);
      }
    } catch (IOException e) {
      platform.getLogger().warn("Failed to load external locale properties file: " + localePropertiesFile);
      e.printStackTrace();
    }
  }
  
  private void loadLocale(String languageCode, Path path, Map<String, Map<String, String>> locales) {
    if (!Files.isRegularFile(path)) {
      platform.getLogger().warn("Cannot find external locale `" + languageCode + "` at `" + path + "`");
      return;
    }
    
    try {
      Properties locale = LocalizationIO.loadProperties(path);
      LocalizationIO.mergeLocale(locales, languageCode, locale);
      platform.getLogger().info("Loaded external locale `" + languageCode + "` from `" + path + "`");
    } catch (IOException e) {
      platform.getLogger().warn("Cannot load external locale `" + languageCode + "`: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
