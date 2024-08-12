package ru.cwcode.cwutils.l10n;

import ru.cwcode.cwutils.logger.Logger;
import ru.cwcode.cwutils.logger.VelocityLogger;

import java.io.File;
import java.io.InputStream;

public class VelocityL10nPlatform implements L10nPlatform {
  private final Object plugin;
  private final File file;
  private final Logger logger;
  
  public VelocityL10nPlatform(Object plugin, org.slf4j.Logger logger, File file) {
    this.plugin = plugin;
    this.file = file;
    this.logger = new VelocityLogger(logger);
  }
  
  @Override
  public File getFile() {
    return file;
  }
  
  @Override
  public Logger getLogger() {
    return logger;
  }
  
  @Override
  public InputStream getResource(String path) {
    return plugin.getClass().getResourceAsStream(path);
  }
}
