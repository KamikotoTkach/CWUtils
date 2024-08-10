package ru.cwcode.cwutils.l10n;

public interface LocalizationRepository {
  String getEntry(String locale, String key);
}
