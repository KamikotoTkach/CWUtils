package ru.cwcode.cwutils.l10n;

import org.bukkit.plugin.java.JavaPlugin;
import ru.cwcode.cwutils.logger.Logger;
import ru.cwcode.cwutils.logger.PaperLogger;

import java.io.File;
import java.io.InputStream;

public class PaperL10nPlatform implements L10nPlatform {
  private final JavaPlugin plugin;
  private final File file;
  private final Logger logger;
  
  public PaperL10nPlatform(JavaPlugin plugin, File file) {
    this.plugin = plugin;
    this.file = file;
    this.logger = new PaperLogger(plugin);
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
    return plugin.getResource(path);
  }
}
