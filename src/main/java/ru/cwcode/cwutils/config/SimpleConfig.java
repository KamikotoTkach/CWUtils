package ru.cwcode.cwutils.config;

import org.jetbrains.annotations.NotNull;
import ru.cwcode.cwutils.l10n.L10nPlatform;
import ru.cwcode.cwutils.text.StringToObjectParser;

import java.io.*;
import java.util.Properties;

public class SimpleConfig {
  private final String configName;
  private final L10nPlatform platform;
  
  Properties fromFile = new Properties();
  Properties fromResources = new Properties();
  
  public SimpleConfig(String configName, L10nPlatform platform) {
    this.configName = configName;
    this.platform = platform;
    
    loadFile();
    loadResources();
    saveToFile();
  }
  
  public String get(String key) {
    return get(key, "[Key %s not found in %s]".formatted(key, configName));
  }
  
  public String get(String key, String defaultVal) {
    String value = fromFile.getProperty(key);
    if (value != null) return value;
    
    value = fromResources.getProperty(key);
    if (value != null) return value;
    
    return defaultVal;
  }
  
  public <T> T getParsed(String key, Class<T> type) {
    return StringToObjectParser.parse(get(key), type);
  }
  
  private void saveToFile() {
    Properties effectiveProperties = new Properties();
    effectiveProperties.putAll(fromResources);
    effectiveProperties.putAll(fromFile);
    
    try (OutputStream os = new FileOutputStream(getConfigFile())) {
      effectiveProperties.store(os, null);
    } catch (Exception e) {
      platform.getLogger().error("Cannot store effective config %s".formatted(configName));
      e.printStackTrace();
    }
  }
  
  private void loadResources() {
    try (InputStream inStream = platform.getResource("config/" + configName + ".properties")) {
      fromResources.load(inStream);
    } catch (Exception e) {
      platform.getLogger().error("Cannot read config from resources: %s".formatted(configName));
      e.printStackTrace();
    }
  }
  
  private void loadFile() {
    fromFile.clear();
    
    File file = getConfigFile();
    if (!file.exists() || !file.isFile()) {
      return;
    }
    
    try (FileInputStream inStream = new FileInputStream(file)) {
      fromFile.load(inStream);
    } catch (Exception e) {
      platform.getLogger().error("Cannot read config from file: %s".formatted(configName));
      e.printStackTrace();
    }
  }
  
  private @NotNull File getConfigFile() {
    return platform.getDataDirectory().resolve(configName + ".properties").toFile();
  }
}
