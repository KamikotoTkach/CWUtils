package ru.cwcode.cwutils.l10n;

import ru.cwcode.cwutils.logger.Logger;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public interface L10nPlatform {
  File getFile();
  
  Path getDataDirectory();
  
  Logger getLogger();
  
  InputStream getResource(String path);
}
