package ru.cwcode.cwutils.l10n.source;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

final class LocalizationIO {
  private LocalizationIO() {
  }
  
  static Properties loadProperties(InputStream inputStream) throws IOException {
    Properties properties = new Properties();
    try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      properties.load(reader);
    }
    return properties;
  }
  
  static Properties loadProperties(Path path) throws IOException {
    Properties properties = new Properties();
    try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      properties.load(reader);
    }
    return properties;
  }
  
  static void mergeLocale(Map<String, Map<String, String>> locales, String languageCode, Properties properties) {
    Map<String, String> localeMap = locales.computeIfAbsent(languageCode, k -> new java.util.HashMap<>());
    for (String key : properties.stringPropertyNames()) {
      localeMap.put(key, properties.getProperty(key));
    }
  }
}
