package ru.cwcode.cwutils.l10n;

import java.util.Set;

public interface LocalizationRepository {
  String getEntry(String locale, String key);
  
  Set<String> getAvailableLocales();
}
