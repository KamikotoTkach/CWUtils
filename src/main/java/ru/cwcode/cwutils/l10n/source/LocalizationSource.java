package ru.cwcode.cwutils.l10n.source;

import java.util.Map;

public interface LocalizationSource {
  void loadInto(Map<String, Map<String, String>> locales);
}
