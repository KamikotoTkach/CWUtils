package ru.cwcode.cwutils.l10n;

import ru.cwcode.cwutils.logger.Logger;

import java.io.File;
import java.io.InputStream;

public interface L10nPlatform {
  File getFile();
  
  Logger getLogger();
  
  InputStream getResource(String path);
}
